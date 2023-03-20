package domain

import (
	"context"
	"time"
)

type FavCompetition struct {
	UserId        int64
	CompetitionId int64
	CreatedAt     time.Time
}

type FavCompetitionRepository interface {
	Create(ctx context.Context, favCompetition FavCompetition) error
	GetByUserId(ctx context.Context, userId int64) ([]FavCompetition, error)
	GetByUserIdAndCompId(ctx context.Context, userId, competitionId int64) (FavCompetition, error)
	Delete(ctx context.Context, userId, competitionId int64) error
}

type FavCompetitionUseCase interface {
	Create(ctx context.Context, userId int64, competitionId int64) error
	GetFavorites(ctx context.Context, userId int64) ([]CompetitionResponse, error)
	Delete(ctx context.Context, userId, competitionId int64) error
}
