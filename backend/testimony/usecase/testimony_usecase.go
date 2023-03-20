package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"github.com/sirupsen/logrus"
	"time"
)

type testimonyUseCase struct {
	testimonyRepository domain.TestimonyRepository
	userRepository      domain.UserRepository
}

func NewTestimonyUseCase(testimonyRepository domain.TestimonyRepository, userRepository domain.UserRepository) domain.TestimonyUseCase {
	return &testimonyUseCase{testimonyRepository, userRepository}
}

func (c *testimonyUseCase) Create(ctx context.Context, req *domain.CreateTestimonyRequest) (domain.TestimonyResponse, error) {

	testimony := domain.Testimony{
		ReceiverId:  req.ReceiverId,
		SenderId:    req.SenderId,
		Description: req.Description,
		Rating:      req.Rating,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}
	if err := c.testimonyRepository.Create(ctx, &testimony); err != nil {
		logrus.Error(err)
		return domain.TestimonyResponse{}, err
	}

	user, err := c.userRepository.GetById(ctx, req.ReceiverId)
	if err != nil {
		return domain.TestimonyResponse{}, err
	}

	user.TotalRating += req.Rating
	user.Votes += 1

	err = c.userRepository.Update(ctx, &user)
	if err != nil {
		return domain.TestimonyResponse{}, err
	}

	receiver, err := c.userRepository.GetById(ctx, req.ReceiverId)
	if err != nil {
		logrus.Error(err)
		return domain.TestimonyResponse{}, err
	}

	sender, err := c.userRepository.GetById(ctx, req.ReceiverId)
	if err != nil {
		logrus.Error(err)
		return domain.TestimonyResponse{}, err
	}

	return domain.TestimonyResponse{
		ID:          testimony.ID,
		Receiver:    domain.UserToUserResponse(receiver),
		Sender:      domain.UserToUserResponse(sender),
		Description: testimony.Description,
		Rating:      testimony.Rating,
		CreatedAt:   testimony.CreatedAt.UnixMilli(),
	}, nil
}

func (c *testimonyUseCase) GetReceivedTestimonies(ctx context.Context, receiverId int64) ([]domain.TestimonyResponse, error) {
	testimonies, err := c.testimonyRepository.GetByReceiverId(ctx, receiverId)
	if err != nil {
		return nil, err
	}

	var res = make([]domain.TestimonyResponse, 0)

	for _, testimony := range testimonies {
		receiver, err := c.userRepository.GetById(ctx, testimony.ReceiverId)
		if err != nil {
			logrus.Error(err)
			return []domain.TestimonyResponse{}, err
		}

		sender, err := c.userRepository.GetById(ctx, testimony.SenderId)
		if err != nil {
			logrus.Error(err)
			return []domain.TestimonyResponse{}, err
		}

		res = append(res, domain.TestimonyResponse{
			ID:          testimony.ID,
			Receiver:    domain.UserToUserResponse(receiver),
			Sender:      domain.UserToUserResponse(sender),
			Description: testimony.Description,
			Rating:      testimony.Rating,
			CreatedAt:   testimony.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}

func (c *testimonyUseCase) GetSentTestimonies(ctx context.Context, senderId int64) ([]domain.TestimonyResponse, error) {
	testimonies, err := c.testimonyRepository.GetBySenderId(ctx, senderId)
	if err != nil {
		return nil, err
	}

	var res = make([]domain.TestimonyResponse, 0)

	for _, testimony := range testimonies {
		receiver, err := c.userRepository.GetById(ctx, testimony.ReceiverId)
		if err != nil {
			logrus.Error(err)
			return []domain.TestimonyResponse{}, err
		}

		sender, err := c.userRepository.GetById(ctx, testimony.SenderId)
		if err != nil {
			logrus.Error(err)
			return []domain.TestimonyResponse{}, err
		}

		res = append(res, domain.TestimonyResponse{
			ID:          testimony.ID,
			Receiver:    domain.UserToUserResponse(receiver),
			Sender:      domain.UserToUserResponse(sender),
			Description: testimony.Description,
			Rating:      testimony.Rating,
			CreatedAt:   testimony.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}
