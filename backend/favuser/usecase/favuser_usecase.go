package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"time"
)

type favoriteUserUseCase struct {
	favoriteUserRepository domain.FavUserRepository
	userUseCase            domain.UserUseCase
	userRepository         domain.UserRepository
}

func NewFavUserUseCase(favoriteUserRepository domain.FavUserRepository, userUseCase domain.UserUseCase, userRepository domain.UserRepository) domain.FavUserUseCase {
	return &favoriteUserUseCase{favoriteUserRepository, userUseCase, userRepository}
}

func (f *favoriteUserUseCase) Create(ctx context.Context, likedId int64, likerId int64) error {
	fav := domain.FavUser{
		LikedId:   likedId,
		LikerId:   likerId,
		CreatedAt: time.Now(),
	}
	err := f.favoriteUserRepository.Create(ctx, fav)
	if err != nil {
		return err
	}
	err = f.userRepository.IncrementLikes(ctx, likerId)
	if err != nil {
		return err
	}

	return nil
}

func (f *favoriteUserUseCase) GetFavorites(ctx context.Context, userId int64) ([]domain.UserResponse, error) {
	favorites, err := f.favoriteUserRepository.GetByLikerId(ctx, userId)
	if err != nil {
		return nil, err
	}

	res := make([]domain.UserResponse, 0)

	for _, fav := range favorites {
		user, err := f.userUseCase.GetUser(ctx, fav.LikedId)
		if err != nil {
			return nil, err
		}

		res = append(res, user)
	}

	return res, nil
}

func (f *favoriteUserUseCase) Delete(ctx context.Context, likedId, likerId int64) error {
	err := f.favoriteUserRepository.Delete(ctx, likedId, likerId)
	if err != nil {
		return err
	}

	err = f.userRepository.DecrementLikes(ctx, likerId)
	if err != nil {
		return err
	}

	return nil
}
