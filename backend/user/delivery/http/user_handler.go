package http

import (
	authHttp "bagus2x/temanlomba/auth/delivery/http"
	"bagus2x/temanlomba/domain"
	"encoding/json"
	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"github.com/valyala/fasthttp"
	"gopkg.in/guregu/null.v4"
	"strconv"
)

type userHandler struct {
	userUseCase domain.UserUseCase
}

func NewUserHandler(router fiber.Router, userUseCase domain.UserUseCase, middleware *authHttp.AuthMiddleware) {
	h := userHandler{userUseCase}

	router.Get("/user/:userId", middleware.Authenticate, h.getUser)
	router.Get("/user", middleware.Authenticate, h.getAuthenticatedUser)
	router.Put("/user", middleware.Authenticate, h.update)
	router.Get("/users", middleware.Authenticate, h.getUsers)
}

func (uh *userHandler) getUser(c *fiber.Ctx) error {
	userId, _ := strconv.ParseInt(c.Params("userId"), 10, 64)

	users, err := uh.userUseCase.GetUser(c.Context(), userId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(users)
}

func (uh *userHandler) getAuthenticatedUser(c *fiber.Ctx) error {
	userId, _ := c.Locals("userId").(int64)

	res, err := uh.userUseCase.GetUser(c.Context(), userId)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(res)
}

func (uh *userHandler) getUsers(c *fiber.Ctx) error {
	users, err := uh.userUseCase.GetUsers(c.Context())
	if err != nil {
		return err
	}

	return c.Status(200).JSON(users)
}

func (uh *userHandler) update(c *fiber.Ctx) error {
	userId, _ := c.Locals("userId").(int64)
	batch, _ := strconv.ParseInt(c.FormValue("batch"), 10, 64)
	age, _ := strconv.ParseInt(c.FormValue("age"), 10, 64)
	achievementsStr := c.FormValue("achievements")
	var achievements = make([]string, 0)
	if len(achievementsStr) > 0 {
		err := json.Unmarshal([]byte(achievementsStr), &achievements)
		if err != nil {
			logrus.Error(err)
			return domain.ErrBadRequest
		}
	}
	certificationsStr := c.FormValue("certifications")
	var certifications = make([]string, 0)
	if len(certificationsStr) > 0 {
		err := json.Unmarshal([]byte(certificationsStr), &certifications)
		if err != nil {
			logrus.Error(err)
			return domain.ErrBadRequest
		}
	}

	skillsStr := c.FormValue("skills")
	var skills = make([]string, 0)
	if len(skillsStr) > 0 {
		err := json.Unmarshal([]byte(skillsStr), &skills)
		if err != nil {
			logrus.Error(err)
			return domain.ErrBadRequest
		}
	}

	invitable := false
	if c.FormValue("invitable") == "true" {
		invitable = true
	}

	photo, err := c.FormFile("photo")
	if err != nil && !errors.Is(err, fasthttp.ErrMissingFile) {
		logrus.Error(err)
		return domain.ErrBadRequest
	}

	req := domain.UpdateUserRequest{
		ID:             userId,
		Name:           c.FormValue("name"),
		Photo:          photo,
		University:     c.FormValue("university"),
		Faculty:        c.FormValue("faculty"),
		StudyProgram:   c.FormValue("studyProgram"),
		Department:     c.FormValue("department"),
		Stream:         c.FormValue("stream"),
		Batch:          null.NewInt(batch, batch != 0),
		Gender:         c.FormValue("gender"),
		Age:            null.NewInt(age, age != 0),
		Bio:            c.FormValue("bio"),
		SKills:         skills,
		Achievements:   achievements,
		Certifications: certifications,
		Invitable:      invitable,
		Location:       null.NewString(c.FormValue("location"), c.FormValue("location") != ""),
	}

	logrus.Println(req)

	user, err := uh.userUseCase.Update(c.Context(), &req)
	if err != nil {
		return err
	}

	return c.Status(200).JSON(user)
}
