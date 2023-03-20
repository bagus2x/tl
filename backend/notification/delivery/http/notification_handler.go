package http

import (
	authHttp "bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
)

type notificationHandler struct {
	notificationUseCase domain.NotificationUseCase
}

func NewNotificationHandler(router fiber.Router, notificationUseCase domain.NotificationUseCase, authMiddleware *authHttp.AuthMiddleware) {
	h := notificationHandler{notificationUseCase}
	router.Get("/notifications", authMiddleware.Authenticate, h.get)
	router.Get("/notifications/unread", authMiddleware.Authenticate, h.countUnread)
}

func (h *notificationHandler) get(c *fiber.Ctx) error {
	userId, _ := c.Locals("userId").(int64)
	res, err := h.notificationUseCase.GetByUserId(c.Context(), userId)
	if err != nil {
		return err
	}

	return c.JSON(res)
}

func (h *notificationHandler) countUnread(c *fiber.Ctx) error {
	userId, _ := c.Locals("userId").(int64)
	res, err := h.notificationUseCase.CountUnread(c.Context(), userId)
	if err != nil {
		return err
	}

	return c.JSON(res)
}
