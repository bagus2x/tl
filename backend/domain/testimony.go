package domain

import (
	"context"
	"time"
)

type Testimony struct {
	ID          int64
	ReceiverId  int64
	SenderId    int64
	Description string
	Rating      float32
	CreatedAt   time.Time
	UpdatedAt   time.Time
}

type TestimonyResponse struct {
	ID          int64        `json:"id"`
	Receiver    UserResponse `json:"receiver"`
	Sender      UserResponse `json:"sender"`
	Description string       `json:"description"`
	Rating      float32      `json:"rating"`
	CreatedAt   int64        `json:"createdAt"`
}

type CreateTestimonyRequest struct {
	ReceiverId  int64   `json:"receiverId"`
	SenderId    int64   `json:"senderId"`
	Description string  `json:"description"`
	Rating      float32 `json:"rating"`
}

type TestimonyRepository interface {
	Create(ctx context.Context, testimony *Testimony) error
	GetBySenderId(ctx context.Context, senderId int64) ([]Testimony, error)
	GetByReceiverId(ctx context.Context, receiverId int64) ([]Testimony, error)
}

type TestimonyUseCase interface {
	Create(ctx context.Context, competition *CreateTestimonyRequest) (TestimonyResponse, error)
	GetSentTestimonies(ctx context.Context, senderId int64) ([]TestimonyResponse, error)
	GetReceivedTestimonies(ctx context.Context, receiver int64) ([]TestimonyResponse, error)
}
