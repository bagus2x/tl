package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"time"
)

type favoriteCompetitionUseCase struct {
	favoriteCompetitionRepository domain.FavCompetitionRepository
	competitionUseCase            domain.CompetitionUseCase
	userRepository                domain.UserRepository
}

func NewFavCompetitionUseCase(favoriteCompetitionRepository domain.FavCompetitionRepository, competitionUseCase domain.CompetitionUseCase, userRepository domain.UserRepository) domain.FavCompetitionUseCase {
	return &favoriteCompetitionUseCase{favoriteCompetitionRepository, competitionUseCase, userRepository}
}

func (f *favoriteCompetitionUseCase) Create(ctx context.Context, userId int64, competitionId int64) error {
	fav := domain.FavCompetition{
		UserId:        userId,
		CompetitionId: competitionId,
		CreatedAt:     time.Now(),
	}
	err := f.favoriteCompetitionRepository.Create(ctx, fav)
	if err != nil {
		return err
	}

	err = f.userRepository.IncrementLikes(ctx, userId)
	if err != nil {
		return err
	}

	return nil
}

func (f *favoriteCompetitionUseCase) GetFavorites(ctx context.Context, userId int64) ([]domain.CompetitionResponse, error) {
	favorites, err := f.favoriteCompetitionRepository.GetByUserId(ctx, userId)
	if err != nil {
		return nil, err
	}

	res := make([]domain.CompetitionResponse, 0)

	for _, fav := range favorites {
		competition, err := f.competitionUseCase.GetCompetition(ctx, fav.CompetitionId)
		if err != nil {
			return nil, err
		}

		res = append(res, competition)
	}

	return res, nil
}

func (f *favoriteCompetitionUseCase) Delete(ctx context.Context, userId, competitionId int64) error {
	err := f.favoriteCompetitionRepository.Delete(ctx, userId, competitionId)
	if err != nil {
		return err
	}

	err = f.userRepository.DecrementLikes(ctx, userId)
	if err != nil {
		return err
	}

	return nil
}
