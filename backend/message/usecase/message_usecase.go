package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"github.com/sirupsen/logrus"
	"time"
)

type messageUseCase struct {
	messageRepository domain.MessageRepository
	userRepository    domain.UserRepository
	timeout           time.Duration
}

func NewMessageUseCase(messageRepository domain.MessageRepository, userRepository domain.UserRepository, timeout time.Duration) domain.MessageUseCase {
	return &messageUseCase{messageRepository, userRepository, timeout}
}

func (c *messageUseCase) Send(ctx context.Context, req *domain.SendMessageRequest) (domain.MessageResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, c.timeout)
	defer cancel()

	message := domain.Message{
		SenderId:    req.SenderId,
		ReceiverId:  req.ReceiverId,
		Description: req.Description,
		Unread:      req.Unread,
		File:        req.File,
		CreatedAt:   time.Now(),
	}

	if err := c.messageRepository.Insert(ctx, &message); err != nil {
		return domain.MessageResponse{}, err
	}

	receiver, err := c.userRepository.GetById(ctx, message.ReceiverId)
	if err != nil {
		logrus.Error(err)
		return domain.MessageResponse{}, err
	}

	sender, err := c.userRepository.GetById(ctx, message.SenderId)
	if err != nil {
		logrus.Error(err)
		return domain.MessageResponse{}, err
	}

	return domain.MessageResponse{
		ID:          message.ID,
		Sender:      domain.UserToUserResponse(sender),
		Receiver:    domain.UserToUserResponse(receiver),
		Description: message.Description,
		Unread:      message.Unread,
		CreatedAt:   message.CreatedAt.UnixMilli(),
	}, nil
}

func (c *messageUseCase) GetMessages(ctx context.Context, userId1 int64, userId2 int64) ([]domain.MessageResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, c.timeout)
	defer cancel()

	messages, err := c.messageRepository.GetByUsersId(ctx, userId1, userId2)
	if err != nil {
		return nil, err
	}

	res := make([]domain.MessageResponse, 0)

	for _, message := range messages {

		userId, _ := ctx.Value("userId").(int64)

		if message.ReceiverId == userId && message.Unread == true {
			message.Unread = false
			err := c.messageRepository.Update(ctx, &message)
			if err != nil {
				return nil, err
			}
		}

		receiver, err := c.userRepository.GetById(ctx, message.ReceiverId)
		if err != nil {
			logrus.Error(err)
			return []domain.MessageResponse{}, err
		}

		sender, err := c.userRepository.GetById(ctx, message.SenderId)
		if err != nil {
			logrus.Error(err)
			return []domain.MessageResponse{}, err
		}

		res = append(res, domain.MessageResponse{
			ID:          message.ID,
			Sender:      domain.UserToUserResponse(sender),
			Receiver:    domain.UserToUserResponse(receiver),
			Description: message.Description,
			Unread:      message.Unread,
			CreatedAt:   message.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}

func (c *messageUseCase) GetLastMessages(ctx context.Context, userId int64) ([]domain.MessageResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, c.timeout)
	defer cancel()

	messages, err := c.messageRepository.GetLastMessagesByUserId(ctx, userId)
	if err != nil {
		return nil, err
	}

	res := make([]domain.MessageResponse, 0)

	for _, message := range messages {
		var totalUnread int
		if message.SenderId != userId {
			totalUnread, err = c.messageRepository.TotalUnread(ctx, message.SenderId, message.ReceiverId)
			if err != nil {
				return nil, err
			}
		}

		receiver, err := c.userRepository.GetById(ctx, message.ReceiverId)
		if err != nil {
			logrus.Error(err)
			return []domain.MessageResponse{}, err
		}

		sender, err := c.userRepository.GetById(ctx, message.SenderId)
		if err != nil {
			logrus.Error(err)
			return []domain.MessageResponse{}, err
		}

		res = append(res, domain.MessageResponse{
			ID:          message.ID,
			Sender:      domain.UserToUserResponse(sender),
			Receiver:    domain.UserToUserResponse(receiver),
			Description: message.Description,
			Unread:      message.Unread,
			TotalUnread: totalUnread,
			CreatedAt:   message.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}
