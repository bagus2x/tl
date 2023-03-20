package http

import (
	"bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"strconv"
)

type favUserHandler struct {
	favUserUseCase domain.FavUserUseCase
}

func NewFavUserHandler(router fiber.Router, favUserUseCase domain.FavUserUseCase, authMiddleware *http.AuthMiddleware) {
	fh := favUserHandler{favUserUseCase}

	router.Post("/user/:likedId/favorite", authMiddleware.Authenticate, fh.createFavorite)
	router.Get("/user/:userId/favorites", authMiddleware.Authenticate, fh.getFavorites)
	router.Delete("/user/:likedId/favorite", authMiddleware.Authenticate, fh.deleteFavorites)
}

func (fh *favUserHandler) createFavorite(c *fiber.Ctx) error {
	likerId, _ := c.Locals("userId").(int64)
	likedId, _ := strconv.ParseInt(c.Params("likedId"), 10, 64)
	err := fh.favUserUseCase.Create(c.Context(), likedId, likerId)
	if err != nil {
		return err
	}

	return c.SendStatus(204)
}

func (fh *favUserHandler) getFavorites(c *fiber.Ctx) error {
	likerId, _ := strconv.ParseInt(c.Params("userId"), 10, 64)
	res, err := fh.favUserUseCase.GetFavorites(c.Context(), likerId)
	if err != nil {
		return err
	}

	return c.JSON(res)
}

func (fh *favUserHandler) deleteFavorites(c *fiber.Ctx) error {
	likerId, _ := c.Locals("userId").(int64)
	likedId, _ := strconv.ParseInt(c.Params("likedId"), 10, 64)
	err := fh.favUserUseCase.Delete(c.Context(), likedId, likerId)
	if err != nil {
		return err
	}

	return c.SendStatus(204)
}
