package http

import (
	"bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"strconv"
)

type competitionHandler struct {
	competitionUseCase domain.CompetitionUseCase
}

func NewCompetitionHandler(router fiber.Router, competitionUseCase domain.CompetitionUseCase, authMiddleware *http.AuthMiddleware) {
	ch := competitionHandler{competitionUseCase}

	router.Post("/competition", authMiddleware.Authenticate, ch.createCompetition)
	router.Get("/competition/:competitionId", authMiddleware.Authenticate, ch.getCompetition)
	router.Get("/competitions", authMiddleware.Authenticate, ch.getCompetitions)
}

func (ch *competitionHandler) createCompetition(c *fiber.Ctx) error {
	poster, err := c.FormFile("poster")
	if err != nil {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrBadRequest, "Cannot upload poster")
	}
	authorId, _ := c.Locals("userId").(int64)
	deadline, _ := strconv.ParseInt(c.FormValue("deadline"), 10, 64)
	minimumFee, _ := strconv.ParseInt(c.FormValue("minimumFee"), 10, 64)
	maximumFee, _ := strconv.ParseInt(c.FormValue("maximumFee"), 10, 64)

	req := domain.CreateCompetitionRequest{
		AuthorId:      authorId,
		Poster:        poster,
		Title:         c.FormValue("title"),
		Description:   c.FormValue("description"),
		Theme:         c.FormValue("theme"),
		City:          c.FormValue("city"),
		Country:       c.FormValue("country"),
		Deadline:      deadline,
		MinimumFee:    minimumFee,
		MaximumFee:    maximumFee,
		Category:      c.FormValue("category"),
		Organizer:     c.FormValue("organizer"),
		OrganizerName: c.FormValue("organizerName"),
		UrlLink:       c.FormValue("urlLink"),
	}

	res, err := ch.competitionUseCase.Create(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.Status(201).JSON(res)
}

func (ch *competitionHandler) getCompetitions(c *fiber.Ctx) error {
	competitions, err := ch.competitionUseCase.GetCompetitions(c.Context())
	if err != nil {
		return err
	}

	return c.Status(200).JSON(competitions)
}

func (ch *competitionHandler) getCompetition(c *fiber.Ctx) error {
	competitionId, _ := strconv.ParseInt(c.Params("competitionId"), 10, 64)

	competition, err := ch.competitionUseCase.GetCompetition(c.Context(), competitionId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(competition)
}
