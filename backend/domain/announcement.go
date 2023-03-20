package domain

import (
	"context"
	"gopkg.in/guregu/null.v4"
	"mime/multipart"
	"time"
)

type Announcement struct {
	ID          int64
	AuthorId    int64
	Description string
	File        null.String
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

type CreateAnnouncementRequest struct {
	AuthorId    int64                 `json:"authorId"`
	Description string                `json:"description"`
	File        *multipart.FileHeader `json:"file" swaggerignore:"true"`
}

type AnnouncementResponse struct {
	ID          int64        `json:"id"`
	Author      UserResponse `json:"author"`
	Description string       `json:"description"`
	File        null.String  `json:"file"`
	CreatedAt   int64        `json:"createdAt"`
}

type AnnouncementRepository interface {
	Create(ctx context.Context, announcement *Announcement) error
	Get(ctx context.Context, authorId int64) ([]Announcement, error)
	GetById(ctx context.Context, announcementId int64) (Announcement, error)
}

type AnnouncementUseCase interface {
	Create(ctx context.Context, req *CreateAnnouncementRequest) (AnnouncementResponse, error)
	GetAnnouncements(ctx context.Context, authorId int64) ([]AnnouncementResponse, error)
	GetAnnouncement(ctx context.Context, announcementId int64) (AnnouncementResponse, error)
}
