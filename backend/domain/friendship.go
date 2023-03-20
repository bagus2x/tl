package domain

import (
	"context"
	"time"
)

type Friendship struct {
	RequesterId int64
	AddresseeId int64
	Accepted    bool
	CreatedAt   time.Time
}

const (
	FriendshipStatusRequested = "friendship_requested"
	FriendshipStatusAccepted  = "friendship_accepted"
)

type FriendshipRepository interface {
	CreateFriendship(ctx context.Context, friendship Friendship) error
	GetFriendship(ctx context.Context, requesterId, addresseeId int64) (Friendship, error)
	GetFriendships(ctx context.Context, userId int64, accepted bool) ([]Friendship, error)
	UpdateFriendship(ctx context.Context, friendship Friendship) error
	DeleteFriendship(ctx context.Context, requesterId, addresseeId int64) error
}

type FriendshipUseCase interface {
	GetFriends(ctx context.Context, userId int64) ([]UserResponse, error)
	RequestFriendship(ctx context.Context, requesterId, addresseeId int64) error
	AcceptFriendship(ctx context.Context, addresseeId, requesterId int64) error
	CancelFriendship(ctx context.Context, userId1, userId2 int64) error
}
