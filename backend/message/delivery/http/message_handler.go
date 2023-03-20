package http

import (
	"bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"strconv"
)

type messageHandler struct {
	messageUseCase domain.MessageUseCase
	authMiddleware *http.AuthMiddleware
}

func NewChatHandler(router fiber.Router, messageUseCase domain.MessageUseCase, authMiddleware *http.AuthMiddleware) {
	h := messageHandler{messageUseCase, authMiddleware}

	router.Get("/messages/last", h.authMiddleware.Authenticate, h.getLastMessages)
	router.Get("/messages/:userId2", h.authMiddleware.Authenticate, h.getMessages)
}

func (ch *messageHandler) getMessages(c *fiber.Ctx) error {
	userId1, _ := c.Locals("userId").(int64)
	userId2, _ := strconv.ParseInt(c.Params("userId2"), 10, 64)

	res, err := ch.messageUseCase.GetMessages(c.Context(), userId1, userId2)
	if err != nil {
		return err
	}

	return c.JSON(res)
}

func (ch *messageHandler) getLastMessages(c *fiber.Ctx) error {
	userId1, _ := c.Locals("userId").(int64)

	res, err := ch.messageUseCase.GetLastMessages(c.Context(), userId1)
	if err != nil {
		return err
	}

	return c.JSON(res)
}
