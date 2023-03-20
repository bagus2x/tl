package http

import (
	"bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"strconv"
)

type friendshipHandler struct {
	friendshipUseCase domain.FriendshipUseCase
}

func NewFriendshipHandler(router fiber.Router, friendshipUseCase domain.FriendshipUseCase, authMiddleware *http.AuthMiddleware) {
	h := friendshipHandler{friendshipUseCase}
	router.Get("/user/:userId/friends", authMiddleware.Authenticate, h.getFriends)
	router.Post("/user/:addresseeId/friend", authMiddleware.Authenticate, h.requestFriendship)
	router.Patch("/user/:requesterId/friend", authMiddleware.Authenticate, h.acceptFriendship)
	router.Delete("/user/:addresseeId/friend", authMiddleware.Authenticate, h.cancelFriendship)
}

func (f *friendshipHandler) getFriends(c *fiber.Ctx) error {
	userId, _ := strconv.ParseInt(c.Params("userId"), 10, 64)

	friends, err := f.friendshipUseCase.GetFriends(c.Context(), userId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(friends)
}

func (f *friendshipHandler) requestFriendship(c *fiber.Ctx) error {
	addresseeId, _ := strconv.ParseInt(c.Params("addresseeId"), 10, 64)
	requesterId, _ := c.Locals("userId").(int64)

	if err := f.friendshipUseCase.RequestFriendship(c.Context(), requesterId, addresseeId); err != nil {
		return err
	}

	return c.SendStatus(204)
}

func (f *friendshipHandler) acceptFriendship(c *fiber.Ctx) error {
	requesterId, _ := strconv.ParseInt(c.Params("requesterId"), 10, 64)
	addresseeId, _ := c.Locals("userId").(int64)

	if err := f.friendshipUseCase.AcceptFriendship(c.Context(), addresseeId, requesterId); err != nil {
		return err
	}

	return c.SendStatus(204)
}

func (f *friendshipHandler) cancelFriendship(c *fiber.Ctx) error {
	addresseeId, _ := strconv.ParseInt(c.Params("addresseeId"), 10, 64)
	requesterId, _ := c.Locals("userId").(int64)

	if err := f.friendshipUseCase.CancelFriendship(c.Context(), addresseeId, requesterId); err != nil {
		return err
	}

	return c.SendStatus(204)
}
