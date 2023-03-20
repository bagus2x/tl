package domain

import (
	"context"
	"gopkg.in/guregu/null.v4"
	"time"
)

type Message struct {
	ID          int64
	SenderId    int64
	ReceiverId  int64
	Description string
	File        null.String
	Unread      bool
	CreatedAt   time.Time
}

type SendMessageRequest struct {
	SenderId    int64       `json:"senderId"`
	ReceiverId  int64       `json:"receiverId"`
	Description string      `json:"description"`
	Unread      bool        `json:"unread"`
	File        null.String `json:"file"`
}

type MessageResponse struct {
	ID          int64        `json:"id"`
	Sender      UserResponse `json:"sender"`
	Receiver    UserResponse `json:"receiver"`
	Description string       `json:"description"`
	File        null.String  `json:"file"`
	Unread      bool         `json:"unread"`
	TotalUnread int          `json:"totalUnread"`
	CreatedAt   int64        `json:"createdAt"`
}

type MessageRepository interface {
	Insert(ctx context.Context, message *Message) error
	GetByUsersId(ctx context.Context, userId1 int64, userId2 int64) ([]Message, error)
	GetLastMessagesByUserId(ctx context.Context, userId1 int64) ([]Message, error)
	Update(ctx context.Context, message *Message) error
	TotalUnread(ctx context.Context, senderId, receiverId int64) (int, error)
}

type MessageUseCase interface {
	Send(ctx context.Context, req *SendMessageRequest) (MessageResponse, error)
	GetMessages(ctx context.Context, userId1 int64, userId2 int64) ([]MessageResponse, error)
	GetLastMessages(ctx context.Context, userId int64) ([]MessageResponse, error)
}
