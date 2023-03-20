package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	"fmt"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"gopkg.in/guregu/null.v4"
	"time"
)

type friendshipUseCase struct {
	friendshipRepository   domain.FriendshipRepository
	userRepository         domain.UserRepository
	favoriteUserRepository domain.FavUserRepository
	notificationUseCase    domain.NotificationUseCase
}

func NewFriendshipUseCase(friendshipRepository domain.FriendshipRepository, userRepository domain.UserRepository, favoriteUserRepository domain.FavUserRepository, notificationUseCase domain.NotificationUseCase) domain.FriendshipUseCase {
	return &friendshipUseCase{friendshipRepository, userRepository, favoriteUserRepository, notificationUseCase}
}

func (f *friendshipUseCase) GetFriends(ctx context.Context, userId int64) ([]domain.UserResponse, error) {
	friendships, err := f.friendshipRepository.GetFriendships(ctx, userId, true)
	if err != nil {
		logrus.Error(err)
		return nil, err
	}

	friendIds := make([]int64, 0)
	for _, friendship := range friendships {
		if friendship.AddresseeId != userId {
			friendIds = append(friendIds, friendship.AddresseeId)
		} else {
			friendIds = append(friendIds, friendship.RequesterId)
		}
	}

	res := make([]domain.UserResponse, 0)

	for _, friendId := range friendIds {
		user, err := f.userRepository.GetById(ctx, friendId)
		if err != nil {
			logrus.Error(err)
			return nil, err
		}

		friendshipStatus, err := f.GetFriendshipStatus(ctx, friendId)
		if err != nil {
			return nil, err
		}

		isFavoriteUser, err := f.IsFavUser(ctx, friendId)
		if err != nil {
			return nil, err
		}

		res = append(res, domain.UserResponse{
			ID:               user.ID,
			Name:             user.Name,
			Email:            user.Email,
			Photo:            user.Photo,
			University:       user.University,
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
			Invitable:        user.Invitable,
			Favorite:         isFavoriteUser,
			Rating:           getRating(user.TotalRating, user.Votes),
			Votes:            user.Votes,
			FriendshipStatus: friendshipStatus,
			UpdatedAt:        user.UpdatedAt.UnixMilli(),
			CreatedAt:        user.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}

func getRating(totalRating float32, votes int32) float32 {
	if votes == 0 {
		return 0
	}
	return totalRating / float32(votes)
}

func (f *friendshipUseCase) RequestFriendship(ctx context.Context, requesterId, addresseeId int64) error {
	if requesterId == addresseeId {
		return errors.WithMessage(domain.ErrBadRequest, "Cannot request friendship with the same ids")
	}

	friendship, err := f.friendshipRepository.GetFriendship(ctx, addresseeId, requesterId)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return err
	}

	// If request has sent by addresseeId (requested), then user will accept automatically
	if friendship != (domain.Friendship{}) && !friendship.Accepted {
		friendship.Accepted = true

		err := f.friendshipRepository.UpdateFriendship(ctx, friendship)
		if err != nil {
			logrus.Error(err)
		}

		err = f.userRepository.IncrementFriends(ctx, requesterId)
		if err != nil {
			return err
		}

		err = f.userRepository.IncrementFriends(ctx, addresseeId)
		if err != nil {
			return err
		}

		return err
	}

	friendship, err = f.friendshipRepository.GetFriendship(ctx, requesterId, addresseeId)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return err
	}

	if friendship != (domain.Friendship{}) {
		return errors.WithMessage(domain.ErrConflict, "Request has sent")
	}

	friendship.RequesterId = requesterId
	friendship.AddresseeId = addresseeId
	friendship.Accepted = false
	friendship.CreatedAt = time.Now()

	err = f.friendshipRepository.CreateFriendship(ctx, friendship)
	if err != nil {
		logrus.Error(err)
		return err
	}

	user, err := f.userRepository.GetById(ctx, friendship.RequesterId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrNotFound, "User not found")
	} else if err != nil {
		logrus.Error(err)
		return err
	}

	notification := domain.CreateNotificationRequest{
		DataId:      null.NewInt(friendship.RequesterId, true),
		UserId:      friendship.AddresseeId,
		Photo:       user.Photo,
		Title:       user.Name,
		Subtitle1:   user.Department,
		Subtitle2:   null.NewString(fmt.Sprintf("%s %d", user.University.String, user.Batch.Int64), user.University.Valid && user.Batch.Valid),
		Description: "friendship_requested",
	}

	err = f.notificationUseCase.Create(ctx, &notification)

	return err
}

func (f *friendshipUseCase) AcceptFriendship(ctx context.Context, addresseeId, requesterId int64) error {
	friendship, err := f.friendshipRepository.GetFriendship(ctx, requesterId, addresseeId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrNotFound, "Request not found")
	} else if err != nil {
		logrus.Error(err)
		return err
	}

	if friendship.RequesterId != requesterId || friendship.Accepted {
		return errors.WithMessage(domain.ErrNotFound, "Request not found")
	}

	friendship.Accepted = true

	err = f.friendshipRepository.UpdateFriendship(ctx, friendship)
	if err != nil {
		return err
	}

	user, err := f.userRepository.GetById(ctx, friendship.AddresseeId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrNotFound, "User not found")
	} else if err != nil {
		logrus.Error(err)
		return err
	}

	notification := domain.CreateNotificationRequest{
		UserId:      friendship.RequesterId,
		Photo:       user.Photo,
		Title:       user.Name,
		Subtitle1:   user.Department,
		Subtitle2:   null.NewString(fmt.Sprintf("%s %d", user.University.String, user.Batch.Int64), user.University.Valid && user.Batch.Valid),
		Description: "friendship_accepted",
	}

	err = f.notificationUseCase.Create(ctx, &notification)
	if err != nil {
		return err
	}

	err = f.userRepository.IncrementFriends(ctx, requesterId)
	if err != nil {
		return err
	}

	err = f.userRepository.IncrementFriends(ctx, addresseeId)
	if err != nil {
		return err
	}

	return nil
}

func (f *friendshipUseCase) CancelFriendship(ctx context.Context, userId1, userId2 int64) error {
	friendship, err := f.friendshipRepository.GetFriendship(ctx, userId1, userId2)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return err
	}

	if friendship == (domain.Friendship{}) {
		friendship, err = f.friendshipRepository.GetFriendship(ctx, userId2, userId1)
		if err != nil && !errors.Is(err, sql.ErrNoRows) {
			return err
		}
	}

	if friendship.Accepted {
		err = f.userRepository.DecrementFriends(ctx, friendship.RequesterId)
		if err != nil {
			return err
		}

		err = f.userRepository.DecrementFriends(ctx, friendship.AddresseeId)
		if err != nil {
			return err
		}
	}

	err = f.friendshipRepository.DeleteFriendship(ctx, userId2, userId1)
	if err != nil {
		return err
	}

	return nil
}

func (f *friendshipUseCase) GetFriendshipStatus(ctx context.Context, userId int64) (null.String, error) {
	var friendshipStatus null.String

	if authUserId, ok := ctx.Value("userId").(int64); ok {
		friendship, err := f.friendshipRepository.GetFriendship(ctx, userId, authUserId)
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

		friendship, err = f.friendshipRepository.GetFriendship(ctx, authUserId, userId)
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

func (f *friendshipUseCase) IsFavUser(ctx context.Context, likedId int64) (bool, error) {
	likerId, _ := ctx.Value("userId").(int64)
	fav, err := f.favoriteUserRepository.GetByLikedIdAndLikerId(ctx, likedId, likerId)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return false, err
	}

	return fav != (domain.FavUser{}), nil
}
