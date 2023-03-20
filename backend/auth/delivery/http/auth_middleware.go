package http

import (
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"strings"
)

type AuthMiddleware struct {
	authUseCase domain.AuthUseCase
}

func NewAuthMiddleware(userUseCase domain.AuthUseCase) AuthMiddleware {
	return AuthMiddleware{authUseCase: userUseCase}
}

func (u *AuthMiddleware) Authenticate(c *fiber.Ctx) error {
	bearer := strings.Split(c.Get("Authorization"), "Bearer ")
	if len(bearer) != 2 {
		return errors.WithMessage(domain.ErrForbidden, "Access token is invalid")
	}

	userId, err := u.authUseCase.CheckAccessToken(bearer[1])
	if err != nil {
		return err
	}

	c.Locals("userId", userId)

	return c.Next()
}
