package domain

import (
	"context"
	"time"
)

type Auth struct {
	ID    int64
	Token string
}

type AuthRepository interface {
	Save(session Auth, duration time.Duration) error
	GetById(id int64) (Auth, error)
	Delete(id int64) error
}

type AuthUseCase interface {
	SignIn(ctx context.Context, req *SignInRequest) (AuthResponse, error)
	SignUp(ctx context.Context, req *SignUpRequest) (AuthResponse, error)
	SignOut(ctx context.Context, userId int64) error
	CheckAccessToken(accessToken string) (int64, error)
	RefreshToken(ctx context.Context, refreshToken string) (AuthResponse, error)
	Verify(ctx context.Context, userId int64, code string) (AuthResponse, error)
	ResendVerificationCode(ctx context.Context, userId int64) error
}

type AuthResponse struct {
	AccessToken  string `json:"accessToken"`
	RefreshToken string `json:"refreshToken"`
	Verified     bool   `json:"verified"`
}

type SignInRequest struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

type SignUpRequest struct {
	Name     string `json:"name"`
	Email    string `json:"email"`
	Password string `json:"password"`
}
