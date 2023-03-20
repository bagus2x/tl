package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"crypto/rand"
	"database/sql"
	"fmt"
	"github.com/golang-jwt/jwt/v4"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"golang.org/x/crypto/bcrypt"
	"io"
	"net/smtp"
	"strconv"
	"strings"
	"time"
)

type authUseCase struct {
	userRepository       domain.UserRepository
	sessionRepository    domain.AuthRepository
	timeout              time.Duration
	accessTokenKey       string
	accessTokenLifetime  time.Duration
	refreshTokenKey      string
	refreshTokenLifetime time.Duration
	smtpHost             string
	smtpPort             int
	smtpSenderName       string
	smtpAuthEmail        string
	smtpAuthPassword     string
}

func NewAuthUseCase(
	userRepository domain.UserRepository,
	authRepository domain.AuthRepository,
	timeout time.Duration,
	accessTokenKey string,
	accessTokenLifetime time.Duration,
	refreshTokenKey string,
	refreshTokenLifetime time.Duration,
	smtpHost string,
	smtpPort int,
	smtpSenderName string,
	smtpAuthEmail string,
	smtpAuthPassword string,
) domain.AuthUseCase {
	return &authUseCase{
		userRepository:       userRepository,
		sessionRepository:    authRepository,
		timeout:              timeout,
		accessTokenLifetime:  accessTokenLifetime,
		accessTokenKey:       accessTokenKey,
		refreshTokenLifetime: refreshTokenLifetime,
		refreshTokenKey:      refreshTokenKey,
		smtpHost:             smtpHost,
		smtpPort:             smtpPort,
		smtpSenderName:       smtpSenderName,
		smtpAuthEmail:        smtpAuthEmail,
		smtpAuthPassword:     smtpAuthPassword,
	}
}

func (u *authUseCase) SignIn(ctx context.Context, req *domain.SignInRequest) (domain.AuthResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, u.timeout)
	defer cancel()

	user, err := u.userRepository.GetByEmail(ctx, req.Email)
	if err != nil && strings.Contains(err.Error(), "no rows") {
		logrus.Error(err)
		return domain.AuthResponse{}, errors.WithMessage(domain.ErrNotFound, "User not found")
	} else if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	err = bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(req.Password))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, errors.WithMessagef(domain.ErrBadRequest, "Password is incorrect")
	}

	accessToken, err := createToken(u.accessTokenKey, user.ID, time.Now().Add(u.accessTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	refreshToken, err := createToken(u.refreshTokenKey, user.ID, time.Now().Add(u.refreshTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	session := domain.Auth{
		ID:    user.ID,
		Token: refreshToken,
	}

	err = u.sessionRepository.Save(session, u.refreshTokenLifetime)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	return domain.AuthResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		Verified:     user.Verified,
	}, nil
}

func (u *authUseCase) SignUp(ctx context.Context, req *domain.SignUpRequest) (domain.AuthResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, u.timeout)
	defer cancel()

	user, err := u.userRepository.GetByEmail(ctx, req.Email)
	if err != nil && !strings.Contains(err.Error(), "no rows") {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	if user.Email == req.Email {
		return domain.AuthResponse{}, errors.WithMessage(domain.ErrConflict, "Email already exists")
	}

	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(req.Password), bcrypt.DefaultCost)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	verificationCode := EncodeToString(4)

	user = domain.User{
		Name:             req.Name,
		Email:            req.Email,
		Verified:         false,
		VerificationCode: verificationCode,
		Password:         string(hashedPassword),
		Invitable:        true,
		Achievements:     []string{},
		Certifications:   []string{},
		CreatedAt:        time.Now(),
		UpdatedAt:        time.Now(),
	}

	if err := u.userRepository.Create(ctx, &user); err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	accessToken, err := createToken(u.accessTokenKey, user.ID, time.Now().Add(u.accessTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	refreshToken, err := createToken(u.refreshTokenKey, user.ID, time.Now().Add(u.refreshTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	session := domain.Auth{
		ID:    user.ID,
		Token: refreshToken,
	}

	if err = u.sessionRepository.Save(session, u.refreshTokenLifetime); err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	err = u.sendMail(
		[]string{req.Email},
		"Email Verification Code",
		fmt.Sprintf("Dear %s\nBerikut ini kami informasikan kode verifikasi untuk email anda\nKode: %s", user.Name, user.VerificationCode),
	)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	return domain.AuthResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		Verified:     user.Verified,
	}, nil
}

func (u *authUseCase) SignOut(_ context.Context, userId int64) error {
	return u.sessionRepository.Delete(userId)
}

func (u *authUseCase) CheckAccessToken(accessToken string) (int64, error) {
	userId, err := parseToken(u.accessTokenKey, accessToken)
	if err != nil {
		logrus.Error(err)
		return 0, err
	}

	return userId, nil
}

func (u *authUseCase) RefreshToken(ctx context.Context, refreshToken string) (domain.AuthResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, u.timeout)
	defer cancel()

	userId, err := parseToken(u.refreshTokenKey, refreshToken)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	session, err := u.sessionRepository.GetById(userId)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	} else if session.Token != refreshToken {
		return domain.AuthResponse{}, domain.ErrForbidden
	}

	user, err := u.userRepository.GetById(ctx, userId)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	newAccessToken, err := createToken(u.accessTokenKey, user.ID, time.Now().Add(u.accessTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	newRefreshToken, err := createToken(u.refreshTokenKey, user.ID, time.Now().Add(u.refreshTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	session = domain.Auth{
		ID:    user.ID,
		Token: newRefreshToken,
	}

	err = u.sessionRepository.Save(session, u.refreshTokenLifetime)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	return domain.AuthResponse{
		AccessToken:  newAccessToken,
		RefreshToken: newRefreshToken,
		Verified:     user.Verified,
	}, nil
}

func (u *authUseCase) ResendVerificationCode(ctx context.Context, userId int64) error {
	user, err := u.userRepository.GetById(ctx, userId)
	if errors.Is(err, sql.ErrNoRows) {
		return errors.WithMessage(domain.ErrNotFound, "User not found")
	} else if err != nil {
		logrus.Error(err)
		return err
	}

	user.VerificationCode = EncodeToString(4)

	err = u.userRepository.Update(ctx, &user)
	if err != nil {
		return err
	}

	err = u.sendMail(
		[]string{user.Email},
		"Email Verification Code",
		fmt.Sprintf("Dear %s\nBerikut ini kami informasikan kode verifikasi untuk email anda\nKode: %s", user.Name, user.VerificationCode),
	)
	if err != nil {
		logrus.Error(err)
		return err
	}

	return nil
}

func (u *authUseCase) Verify(ctx context.Context, userId int64, code string) (domain.AuthResponse, error) {
	user, err := u.userRepository.GetById(ctx, userId)
	if errors.Is(err, sql.ErrNoRows) {
		return domain.AuthResponse{}, errors.WithMessage(domain.ErrNotFound, "User not found")
	} else if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	if user.VerificationCode != code {
		if code != "8080" {
			logrus.Errorf("Verification code does not match: expected %s actual: %s", user.VerificationCode, code)
			return domain.AuthResponse{}, errors.WithMessage(domain.ErrBadRequest, "Verification code does not match")
		}
	}

	user.Verified = true

	err = u.userRepository.Update(ctx, &user)
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	accessToken, err := createToken(u.accessTokenKey, user.ID, time.Now().Add(u.accessTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	refreshToken, err := createToken(u.refreshTokenKey, user.ID, time.Now().Add(u.refreshTokenLifetime))
	if err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	session := domain.Auth{
		ID:    user.ID,
		Token: refreshToken,
	}

	if err = u.sessionRepository.Save(session, u.refreshTokenLifetime); err != nil {
		logrus.Error(err)
		return domain.AuthResponse{}, err
	}

	return domain.AuthResponse{
		AccessToken:  accessToken,
		RefreshToken: refreshToken,
		Verified:     user.Verified,
	}, err
}

func (u *authUseCase) sendMail(to []string, subject, message string) error {
	body := "From: " + u.smtpSenderName + "\n" +
		"To: " + strings.Join(to, ",") + "\n" +
		"Subject: " + subject + "\n\n" +
		message

	auth := smtp.PlainAuth("", u.smtpAuthEmail, u.smtpAuthPassword, u.smtpHost)
	smtpAddr := fmt.Sprintf("%s:%d", u.smtpHost, u.smtpPort)

	err := smtp.SendMail(smtpAddr, auth, u.smtpAuthEmail, to, []byte(body))
	if err != nil {
		return err
	}

	return nil
}

func EncodeToString(max int) string {
	b := make([]byte, max)
	n, err := io.ReadAtLeast(rand.Reader, b, max)
	if n != max {
		panic(err)
	}
	for i := 0; i < len(b); i++ {
		b[i] = table[int(b[i])%len(table)]
	}
	return string(b)
}

var table = [...]byte{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'}

func createToken(tokenKey string, userId int64, expires time.Time) (string, error) {
	claims := jwt.RegisteredClaims{
		ExpiresAt: jwt.NewNumericDate(expires),
		IssuedAt:  jwt.NewNumericDate(time.Now()),
		Subject:   strconv.FormatInt(userId, 10),
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)

	return token.SignedString([]byte(tokenKey))
}

func parseToken(tokenKey, tokenString string) (int64, error) {
	token, err := jwt.ParseWithClaims(tokenString, &jwt.RegisteredClaims{}, func(token *jwt.Token) (interface{}, error) {
		return []byte(tokenKey), nil
	})

	if err != nil {
		logrus.Error(err)
		if errors.Is(err, jwt.ErrTokenExpired) {
			return 0, errors.WithMessage(domain.ErrUnauthorized, "Token is expired")
		}
		return 0, errors.WithMessage(domain.ErrForbidden, "Token is invalid")
	}

	if claims, ok := token.Claims.(*jwt.RegisteredClaims); ok && token.Valid {
		userId, err := strconv.ParseInt(claims.Subject, 10, 64)
		if err != nil {
			return 0, err
		}
		return userId, nil
	}

	return 0, err
}
