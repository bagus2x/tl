package app

import (
	"bagus2x/temanlomba/domain"
	"fmt"
	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"strings"
)

func errorHandler(ctx *fiber.Ctx, err error) error {
	res := ParseError(err)
	return ctx.Status(res.Code).JSON(res)
}

func error404handler(ctx *fiber.Ctx) error {
	return ctx.Status(404).JSON(domain.NewError(fmt.Sprintf("Cannot %s %s", ctx.Method(), ctx.Path()), 404))
}

func ParseError(err error) *domain.Error {
	if err == nil {
		return nil
	}

	msg := strings.Split(err.Error(), ":")

	if err := errors.Cause(err); err != nil {
		if res, ok := err.(*domain.Error); ok {
			if len(msg) != 0 {
				res.Message = msg[0]
			}
			return res
		}
	}

	if res, ok := err.(*domain.Error); ok {
		return res
	}

	return &domain.Error{
		Message: "internal server error",
		Code:    500,
	}
}
