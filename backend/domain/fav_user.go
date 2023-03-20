package domain

import (
	"context"
	"time"
)

type FavUser struct {
	LikedId   int64
	LikerId   int64
	CreatedAt time.Time
}

type FavUserRepository interface {
	Create(ctx context.Context, favUser FavUser) error
	GetByLikerId(ctx context.Context, userId int64) ([]FavUser, error)
	GetByLikedIdAndLikerId(ctx context.Context, userId, competitionId int64) (FavUser, error)
	Delete(ctx context.Context, userId, competitionId int64) error
}

type FavUserUseCase interface {
	Create(ctx context.Context, userId int64, competitionId int64) error
	GetFavorites(ctx context.Context, userId int64) ([]UserResponse, error)
	Delete(ctx context.Context, userId, competitionId int64) error
}
