package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	"fmt"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"gopkg.in/guregu/null.v4"
	"strings"
	"time"
)

type userUseCase struct {
	userRepository         domain.UserRepository
	friendshipRepository   domain.FriendshipRepository
	favoriteUserRepository domain.FavUserRepository
	storageUseCase         domain.StorageUseCase
	timeout                time.Duration
}

func NewUserUseCase(
	userRepository domain.UserRepository,
	friendshipRepository domain.FriendshipRepository,
	favoriteUserRepository domain.FavUserRepository,
	storageUseCase domain.StorageUseCase,
	timeout time.Duration,
) domain.UserUseCase {
	return &userUseCase{
		userRepository:         userRepository,
		friendshipRepository:   friendshipRepository,
		favoriteUserRepository: favoriteUserRepository,
		storageUseCase:         storageUseCase,
		timeout:                timeout,
	}
}

func (u *userUseCase) GetUsers(ctx context.Context) ([]domain.UserResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, u.timeout)
	defer cancel()

	users, err := u.userRepository.Get(ctx)
	if err != nil {
		logrus.Error(err)
		return nil, err
	}

	res := make([]domain.UserResponse, 0)

	for _, user := range users {

		// Check friendship
		friendshipStatus, err := u.GetFriendshipStatus(ctx, user.ID)
		if err != nil {
			return nil, err
		}

		isFavorite, err := u.IsFavUser(ctx, user.ID)
		if err != nil {
			return nil, err
		}

		res = append(res, domain.UserResponse{
			ID:               user.ID,
			Name:             user.Name,
			Photo:            user.Photo,
			Email:            user.Email,
			University:       user.University,
			Faculty:          user.Faculty,
			StudyProgram:     user.StudyProgram,
			Department:       user.Department,
			Stream:           user.Stream,
			Batch:            user.Batch,
			Gender:           user.Gender,
			Age:              user.Age,
			Bio:              user.Bio,
			Achievements:     user.Achievements,
			Certifications:   user.Certifications,
			SKills:           user.SKills,
			FriendshipStatus: friendshipStatus,
			Favorite:         isFavorite,
			Invitable:        user.Invitable,
			Location:         user.Location,
			Rating:           getRating(user.TotalRating, user.Votes),
			Votes:            user.Votes,
			Likes:            user.Likes,
			Friends:          user.Friends,
			UpdatedAt:        user.UpdatedAt.UnixMilli(),
			CreatedAt:        user.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}

func (u *userUseCase) GetUser(ctx context.Context, userId int64) (domain.UserResponse, error) {
	ctx, cancel := context.WithTimeout(ctx, u.timeout)
	defer cancel()

	user, err := u.userRepository.GetById(ctx, userId)
	if err != nil && strings.Contains(err.Error(), "no rows") {
		logrus.Error(err)
		return domain.UserResponse{}, errors.WithMessage(domain.ErrNotFound, "User not found")
	} else if err != nil {
		logrus.Error(err)
		return domain.UserResponse{}, err
	}

	// Check friendship
	friendshipStatus, err := u.GetFriendshipStatus(ctx, userId)
	if err != nil {
		return domain.UserResponse{}, err
	}

	isFavorite, err := u.IsFavUser(ctx, user.ID)
	if err != nil {
		return domain.UserResponse{}, err
	}

	return domain.UserResponse{
		ID:               user.ID,
		Name:             user.Name,
		Photo:            user.Photo,
		Email:            user.Email,
		University:       user.University,
		Faculty:          user.Faculty,
		StudyProgram:     user.StudyProgram,
		Department:       user.Department,
		Stream:           user.Stream,
		Batch:            user.Batch,
		Gender:           user.Gender,
		Age:              user.Age,
		Bio:              user.Bio,
		Achievements:     user.Achievements,
		Certifications:   user.Certifications,
		SKills:           user.SKills,
		FriendshipStatus: friendshipStatus,
		Favorite:         isFavorite,
		Invitable:        user.Invitable,
		Location:         user.Location,
		Rating:           getRating(user.TotalRating, user.Votes),
		Votes:            user.Votes,
		Likes:            user.Likes,
		Friends:          user.Friends,
		UpdatedAt:        user.UpdatedAt.UnixMilli(),
		CreatedAt:        user.CreatedAt.UnixMilli(),
	}, nil
}

func (u *userUseCase) GetFriendshipStatus(ctx context.Context, userId int64) (null.String, error) {
	var friendshipStatus null.String

	if authUserId, ok := ctx.Value("userId").(int64); ok {
		friendship, err := u.friendshipRepository.GetFriendship(ctx, userId, authUserId)
		if err != nil && !errors.Is(err, sql.ErrNoRows) {
			logrus.Error(err)
			return null.String{}, err
		}

		if friendship != (domain.Friendship{}) {
			if friendship.Accepted {
				friendshipStatus = null.NewString(domain.FriendshipStatusAccepted, true)
			} else {
				friendshipStatus = null.NewString(domain.FriendshipStatusRequested, false)
			}
		}

		friendship, err = u.friendshipRepository.GetFriendship(ctx, authUserId, userId)
		if err != nil && !errors.Is(err, sql.ErrNoRows) {
			logrus.Error(err)
			return null.String{}, err
		}

		if friendship != (domain.Friendship{}) {
			if friendship.Accepted {
				friendshipStatus = null.NewString(domain.FriendshipStatusAccepted, true)
			} else {
				friendshipStatus = null.NewString(domain.FriendshipStatusRequested, true)
			}
		}
	}

	return friendshipStatus, nil
}

func (u *userUseCase) IsFavUser(ctx context.Context, likedId int64) (bool, error) {
	likerId, _ := ctx.Value("userId").(int64)
	fav, err := u.favoriteUserRepository.GetByLikedIdAndLikerId(ctx, likedId, likerId)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return false, err
	}

	return fav != (domain.FavUser{}), nil
}

func (u *userUseCase) Update(ctx context.Context, req *domain.UpdateUserRequest) (domain.UserResponse, error) {
	user, err := u.userRepository.GetById(ctx, req.ID)
	if err != nil {
		return domain.UserResponse{}, err
	}

	user.Name = req.Name
	if req.Photo != nil {
		storage := domain.Storage{
			File:   req.Photo,
			Name:   fmt.Sprintf("%d-%d", time.Now().UnixMilli(), req.ID),
			Folder: "photo",
		}

		photo, err := u.storageUseCase.ImageUpload(ctx, &storage)
		if err != nil {
			return domain.UserResponse{}, err
		}

		user.Photo = null.NewString(photo, true)
	}
	user.University = null.NewString(req.University, true)
	user.Faculty = null.NewString(req.Faculty, true)
	user.StudyProgram = null.NewString(req.StudyProgram, true)
	user.Department = null.NewString(req.Department, true)
	user.Stream = null.NewString(req.Stream, true)
	user.Batch = req.Batch
	user.Gender = null.NewString(req.Gender, true)
	user.Age = req.Age
	user.Bio = null.NewString(req.Bio, true)
	user.SKills = req.SKills
	user.Achievements = req.Achievements
	user.Certifications = req.Certifications
	user.Invitable = req.Invitable
	user.Location = req.Location
	user.UpdatedAt = time.Now()

	err = u.userRepository.Update(ctx, &user)
	if err != nil {
		return domain.UserResponse{}, err
	}

	return domain.UserResponse{
		ID:             user.ID,
		Name:           user.Name,
		Photo:          user.Photo,
		Email:          user.Email,
		University:     user.University,
		Faculty:        user.Faculty,
		StudyProgram:   user.StudyProgram,
		Department:     user.Department,
		Stream:         user.Stream,
		Batch:          user.Batch,
		Gender:         user.Gender,
		Age:            user.Age,
		Bio:            user.Bio,
		Achievements:   user.Achievements,
		Certifications: user.Certifications,
		SKills:         user.SKills,
		Invitable:      user.Invitable,
		Location:       user.Location,
		Rating:         getRating(user.TotalRating, user.Votes),
		Votes:          user.Votes,
		Likes:          user.Likes,
		Friends:        user.Friends,
		UpdatedAt:      user.UpdatedAt.UnixMilli(),
		CreatedAt:      user.CreatedAt.UnixMilli(),
	}, nil
}

func getRating(totalRating float32, votes int32) float32 {
	if votes == 0 {
		return 0
	}
	return totalRating / float32(votes)
}
