package http

import (
	"bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"strconv"
)

type testimonyHandler struct {
	testimonyUseCase domain.TestimonyUseCase
}

func NewTestimonyHandler(router fiber.Router, testimonyUseCase domain.TestimonyUseCase, authMiddleware *http.AuthMiddleware) {
	ch := testimonyHandler{testimonyUseCase}

	router.Post("/testimony", authMiddleware.Authenticate, ch.createTestimony)
	router.Get("/testimonies/:receiverId", authMiddleware.Authenticate, ch.getReceivedTestimony)
	router.Get("/testimonies", authMiddleware.Authenticate, ch.getSentTestimonies)
}

func (ch *testimonyHandler) createTestimony(c *fiber.Ctx) error {
	var req domain.CreateTestimonyRequest
	if err := c.BodyParser(&req); err != nil {
		return err
	}

	senderId, _ := c.Locals("userId").(int64)
	req.SenderId = senderId

	res, err := ch.testimonyUseCase.Create(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.Status(201).JSON(res)
}

func (ch *testimonyHandler) getSentTestimonies(c *fiber.Ctx) error {
	senderId, _ := c.Locals("userId").(int64)
	testimonies, err := ch.testimonyUseCase.GetSentTestimonies(c.Context(), senderId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(testimonies)
}

func (ch *testimonyHandler) getReceivedTestimony(c *fiber.Ctx) error {
	receiverId, _ := strconv.ParseInt(c.Params("receiverId"), 10, 64)

	testimony, err := ch.testimonyUseCase.GetReceivedTestimonies(c.Context(), receiverId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(testimony)
}
