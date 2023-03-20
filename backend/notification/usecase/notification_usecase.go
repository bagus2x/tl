package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"github.com/sirupsen/logrus"
	"time"
)

type notificationUseCase struct {
	notificationRepository domain.NotificationRepository
}

func NewNotificationUseCase(notificationRepository domain.NotificationRepository) domain.NotificationUseCase {
	return &notificationUseCase{notificationRepository}
}

func (n *notificationUseCase) Create(ctx context.Context, req *domain.CreateNotificationRequest) error {
	notification := domain.Notification{
		UserId:      req.UserId,
		DataId:      req.DataId,
		Photo:       req.Photo,
		Title:       req.Title,
		Subtitle1:   req.Subtitle1,
		Subtitle2:   req.Subtitle2,
		Description: req.Description,
		Unread:      true,
		CreatedAt:   time.Now(),
	}

	err := n.notificationRepository.Create(ctx, &notification)

	return err
}

func (n *notificationUseCase) GetByUserId(ctx context.Context, userId int64) ([]domain.NotificationResponse, error) {
	notifications, err := n.notificationRepository.GetByUserId(ctx, userId)
	if err != nil {
		return nil, err
	}

	var res = make([]domain.NotificationResponse, 0)

	for _, notification := range notifications {
		if notification.Unread {
			logrus.Info("ID ", notification.ID, " unread ", notification.Unread)
			notification.Unread = false
			err := n.notificationRepository.Update(ctx, &notification)
			if err != nil {
				logrus.Error(err)
			}
		}

		res = append(res, domain.NotificationResponse{
			ID:          notification.ID,
			DataId:      notification.DataId,
			Photo:       notification.Photo,
			Title:       notification.Title,
			Subtitle1:   notification.Subtitle1,
			Subtitle2:   notification.Subtitle2,
			Description: notification.Description,
			CreatedAt:   notification.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}

func (n *notificationUseCase) CountUnread(ctx context.Context, userId int64) (int, error) {
	return n.notificationRepository.CountUnread(ctx, userId)
}
