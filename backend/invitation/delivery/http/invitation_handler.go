package http

import (
	authHttp "bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"github.com/valyala/fasthttp"
	"strconv"
)

type invitationHandler struct {
	invitationUseCase domain.InvitationUseCase
}

func NewInvitationHandler(router fiber.Router, invitationUseCase domain.InvitationUseCase, authMiddleware *authHttp.AuthMiddleware) {
	h := invitationHandler{invitationUseCase}
	router.Post("/invitation", authMiddleware.Authenticate, h.invite)
	router.Patch("/invitation/:invitationId", authMiddleware.Authenticate, h.respond)
	router.Get("/invitation/:invitationId", authMiddleware.Authenticate, h.getInvitation)
}

func (i *invitationHandler) invite(c *fiber.Ctx) error {
	inviterId, _ := c.Locals("userId").(int64)
	inviteeId, _ := strconv.ParseInt(c.FormValue("invitee_id"), 10, 64)
	file, err := c.FormFile("file")
	if err != nil && !errors.Is(err, fasthttp.ErrMissingFile) {
		return err
	}

	req := domain.InviteRequest{
		InviterId:   inviterId,
		InviteeId:   inviteeId,
		Description: c.FormValue("description"),
		File:        file,
	}

	err = c.BodyParser(&req)
	if err != nil {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrBadRequest, err.Error())
	}

	err = i.invitationUseCase.Invite(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.SendStatus(204)
}

func (i *invitationHandler) respond(c *fiber.Ctx) error {
	var req domain.RespondRequest

	err := c.BodyParser(&req)
	if err != nil {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrBadRequest, err.Error())
	}

	invitationId, _ := strconv.ParseInt(c.Params("invitationId"), 10, 64)
	req.ID = invitationId

	inviteeId, _ := c.Locals("userId").(int64)
	req.InviteeId = inviteeId

	err = i.invitationUseCase.Respond(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.SendStatus(204)
}

func (i *invitationHandler) getInvitation(c *fiber.Ctx) error {
	invitationId, _ := strconv.ParseInt(c.Params("invitationId"), 10, 64)

	res, err := i.invitationUseCase.GetInvitation(c.Context(), invitationId)
	if err != nil {
		return err
	}

	return c.JSON(res)
}
