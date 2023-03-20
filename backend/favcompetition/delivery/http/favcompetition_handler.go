package http

import (
	"bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"strconv"
)

type favCompetitionHandler struct {
	favCompetitionUseCase domain.FavCompetitionUseCase
}

func NewFavCompetitionHandler(router fiber.Router, favCompetitionUseCase domain.FavCompetitionUseCase, authMiddleware *http.AuthMiddleware) {
	fh := favCompetitionHandler{favCompetitionUseCase}

	router.Post("/competition/:competitionId/favorite", authMiddleware.Authenticate, fh.createFavorite)
	router.Get("/competitions/favorites/:userId", authMiddleware.Authenticate, fh.getFavorites)
	router.Delete("/competition/:competitionId/favorite", authMiddleware.Authenticate, fh.deleteFavorites)
}

func (fh *favCompetitionHandler) createFavorite(c *fiber.Ctx) error {
	userId, _ := c.Locals("userId").(int64)
	competitionId, _ := strconv.ParseInt(c.Params("competitionId"), 10, 64)
	err := fh.favCompetitionUseCase.Create(c.Context(), userId, competitionId)
	if err != nil {
		return err
	}

	return c.SendStatus(204)
}

func (fh *favCompetitionHandler) getFavorites(c *fiber.Ctx) error {
	userId, _ := strconv.ParseInt(c.Params("userId"), 10, 64)
	res, err := fh.favCompetitionUseCase.GetFavorites(c.Context(), userId)
	if err != nil {
		return err
	}

	return c.JSON(res)
}

func (fh *favCompetitionHandler) deleteFavorites(c *fiber.Ctx) error {
	userId, _ := c.Locals("userId").(int64)
	competitionId, _ := strconv.ParseInt(c.Params("competitionId"), 10, 64)
	err := fh.favCompetitionUseCase.Delete(c.Context(), userId, competitionId)
	if err != nil {
		return err
	}

	return c.SendStatus(204)
}
