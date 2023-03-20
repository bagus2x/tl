package domain

import (
	"context"
	"gopkg.in/guregu/null.v4"
	"time"
)

type Notification struct {
	ID          int64
	UserId      int64
	DataId      null.Int
	Photo       null.String
	Title       string
	Subtitle1   null.String
	Subtitle2   null.String
	Description string
	Unread      bool
	CreatedAt   time.Time
}

type CreateNotificationRequest struct {
	UserId      int64       `json:"userId"`
	DataId      null.Int    `json:"dataId"`
	Photo       null.String `json:"photo"`
	Title       string      `json:"title"`
	Subtitle1   null.String `json:"subTitle1"`
	Subtitle2   null.String `json:"subtitle2"`
	Description string      `json:"description"`
}

type NotificationResponse struct {
	ID          int64       `json:"id"`
	DataId      null.Int    `json:"dataId"`
	Photo       null.String `json:"photo"`
	Title       string      `json:"title"`
	Subtitle1   null.String `json:"subtitle1"`
	Subtitle2   null.String `json:"subtitle2"`
	Description string      `json:"description"`
	CreatedAt   int64       `json:"createdAt"`
}

type NotificationRepository interface {
	Create(ctx context.Context, notification *Notification) error
	GetByUserId(ctx context.Context, userId int64) ([]Notification, error)
	Update(ctx context.Context, notification *Notification) error
	CountUnread(ctx context.Context, userId int64) (int, error)
}

type NotificationUseCase interface {
	Create(ctx context.Context, req *CreateNotificationRequest) error
	GetByUserId(ctx context.Context, userId int64) ([]NotificationResponse, error)
	CountUnread(ctx context.Context, userId int64) (int, error)
}
