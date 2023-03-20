package http

import (
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
)

type authHandler struct {
	userUseCase    domain.AuthUseCase
	userMiddleware *AuthMiddleware
}

func NewAuthHandler(router fiber.Router, authUseCase domain.AuthUseCase, userMiddleware *AuthMiddleware) {
	handler := authHandler{authUseCase, userMiddleware}

	router.Get("/auth/refresh", handler.refreshToken)
	router.Post("/auth/signup", handler.signUp)
	router.Post("/auth/signin", handler.signIn)
	router.Delete("/auth/signout", userMiddleware.Authenticate, handler.signOut)
	router.Patch("/auth/verification-code", userMiddleware.Authenticate, handler.verify)
	router.Get("/auth/verification-code", userMiddleware.Authenticate, handler.resendVerificationCode)
}

// @Tags        Auth
// @Summary     Sign Up
// @Accept      json
// @Produce     json
// @Param       Request body     domain.SignUpRequest true "Model"
// @Success     201     {object} domain.AuthResponse  "created"
// @Failure     409     {object} domain.Error         "conflict"
// @Router      /auth/signup [post]
func (h *authHandler) signUp(c *fiber.Ctx) error {
	var req domain.SignUpRequest

	if err := c.BodyParser(&req); err != nil {
		return err
	}

	res, err := h.userUseCase.SignUp(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.Status(201).JSON(res)
}

// @Tags        Auth
// @Summary     Sign In
// @Accept      json
// @Produce     json
// @Param       Request body     domain.SignInRequest true "Model"
// @Success     200     {object} domain.AuthResponse  "ok"
// @Failure     404     {object} domain.Error         "user not found"
// @Router      /auth/signin [post]
func (h *authHandler) signIn(c *fiber.Ctx) error {
	var req domain.SignInRequest

	if err := c.BodyParser(&req); err != nil {
		return err
	}

	res, err := h.userUseCase.SignIn(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(res)
}

// @Summary     Sign Out
// @Tags        Auth
// @Accept      json
// @Produce     json
// @Param       Authorization header string true "Bearer"
// @Success     204
// @Failure     400 {object} domain.Error
// @Router      /auth/signout [delete]
func (h *authHandler) signOut(c *fiber.Ctx) error {
	userId, _ := c.Locals("userId").(int64)

	if err := h.userUseCase.SignOut(c.Context(), userId); err != nil {
		return err
	}

	return c.SendStatus(204)
}

// @Summary     Refresh Token
// @Tags        Auth
// @Accept      json
// @Produce     json
// @Success     200     {object} domain.AuthResponse  "ok"
// @Failure     400 {object} domain.Error
// @Router      /auth/refresh [get]
func (h *authHandler) refreshToken(c *fiber.Ctx) error {
	refreshToken := c.Get("X-Refresh-Token")

	res, err := h.userUseCase.RefreshToken(c.Context(), refreshToken)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(res)
}

func (h *authHandler) verify(c *fiber.Ctx) error {
	verificationCode := c.Get("Verification-Code")

	userId, _ := c.Locals("userId").(int64)

	res, err := h.userUseCase.Verify(c.Context(), userId, verificationCode)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(res)
}

func (h *authHandler) resendVerificationCode(c *fiber.Ctx) error {

	userId, _ := c.Locals("userId").(int64)

	err := h.userUseCase.ResendVerificationCode(c.Context(), userId)
	if err != nil {
		return err
	}

	return c.SendStatus(204)
}
