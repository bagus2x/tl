package http

import (
	"bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"github.com/valyala/fasthttp"
	"strconv"
)

type announcementHandler struct {
	announcementUseCase domain.AnnouncementUseCase
}

func NewAnnouncementHandler(router fiber.Router, announcementUseCase domain.AnnouncementUseCase, authMiddleware *http.AuthMiddleware) {
	ch := announcementHandler{announcementUseCase}

	router.Post("/announcement", authMiddleware.Authenticate, ch.createAnnouncement)
	router.Get("/announcement/:announcementId", authMiddleware.Authenticate, ch.getAnnouncement)
	router.Get("/announcements", authMiddleware.Authenticate, ch.getAnnouncements)
}

func (ch *announcementHandler) createAnnouncement(c *fiber.Ctx) error {
	file, err := c.FormFile("file")
	// The file is optional
	if err != nil && !errors.Is(err, fasthttp.ErrMissingFile) {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrBadRequest, "Cannot upload file")
	}
	authorId, _ := c.Locals("userId").(int64)

	req := domain.CreateAnnouncementRequest{
		AuthorId:    authorId,
		File:        file,
		Description: c.FormValue("description"),
	}

	res, err := ch.announcementUseCase.Create(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.Status(201).JSON(res)
}

func (ch *announcementHandler) getAnnouncements(c *fiber.Ctx) error {
	authorId, _ := strconv.ParseInt(c.Query("authorId"), 10, 64)
	announcements, err := ch.announcementUseCase.GetAnnouncements(c.Context(), authorId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(announcements)
}

func (ch *announcementHandler) getAnnouncement(c *fiber.Ctx) error {
	announcementId, _ := strconv.ParseInt(c.Params("announcementId"), 10, 64)

	announcement, err := ch.announcementUseCase.GetAnnouncement(c.Context(), announcementId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(announcement)
}
