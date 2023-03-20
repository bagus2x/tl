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

type invitationUseCase struct {
	invitationRepository domain.InvitationRepository
	storageUseCase       domain.StorageUseCase
	notificationUseCase  domain.NotificationUseCase
	userRepository       domain.UserRepository
}

func NewInvitationUseCase(invitationRepository domain.InvitationRepository, storageUseCase domain.StorageUseCase, notificationUseCase domain.NotificationUseCase, userRepository domain.UserRepository) domain.InvitationUseCase {
	return &invitationUseCase{invitationRepository, storageUseCase, notificationUseCase, userRepository}
}

func (i *invitationUseCase) Invite(ctx context.Context, req *domain.InviteRequest) error {
	var fileUrl null.String

	if req.File != nil {
		storage := domain.Storage{
			File:   req.File,
			Name:   fmt.Sprintf("%d-%d", time.Now().UnixMilli(), req.InviterId),
			Folder: "invitation",
		}
		file, err := i.storageUseCase.ImageUpload(ctx, &storage)
		if err != nil {
			return err
		}
		fileUrl = null.NewString(file, file != "")
	}

	invitation := domain.Invitation{
		InviterId:   req.InviterId,
		InviteeId:   req.InviteeId,
		Description: req.Description,
		File:        fileUrl,
		Status:      domain.InvitationStatusRequested,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	err := i.invitationRepository.Create(ctx, &invitation)
	if err != nil {
		return err
	}

	user, err := i.userRepository.GetById(ctx, invitation.InviterId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrNotFound, "User not found")
	} else if err != nil {
		return err
	}

	notification := domain.CreateNotificationRequest{
		UserId:      invitation.InviteeId,
		DataId:      null.NewInt(invitation.ID, true),
		Photo:       user.Photo,
		Title:       user.Name,
		Subtitle1:   user.Department,
		Subtitle2:   null.NewString(fmt.Sprintf("%s %d", user.University.String, user.Batch.Int64), user.University.Valid && user.Batch.Valid),
		Description: "invitation_requested",
	}

	err = i.notificationUseCase.Create(ctx, &notification)
	if err != nil {
		return err
	}

	return nil
}

func (i *invitationUseCase) Respond(ctx context.Context, req *domain.RespondRequest) error {
	invitation, err := i.invitationRepository.GetById(ctx, req.ID)
	if err != nil {
		return err
	}

	if invitation.Status == domain.InvitationStatusAccepted || invitation.Status == domain.InvitationStatusDeclined {
		return errors.WithMessage(domain.ErrBadRequest, "Response has been sent")
	}

	if req.InviteeId != invitation.InviteeId {
		return errors.WithMessage(domain.ErrNotFound, "Invitation not found")
	}

	if !req.Status.Valid() {
		return errors.WithMessage(domain.ErrBadRequest, "Valid values are accepted and declined")
	}

	invitation.Response = req.Response
	invitation.Status = req.Status
	invitation.UpdatedAt = time.Now()

	err = i.invitationRepository.Update(ctx, &invitation)

	user, err := i.userRepository.GetById(ctx, invitation.InviteeId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return errors.WithMessage(domain.ErrNotFound, "User not found")
	}

	notification := domain.CreateNotificationRequest{
		UserId:      invitation.InviterId,
		DataId:      null.NewInt(invitation.ID, true),
		Photo:       user.Photo,
		Title:       user.Name,
		Subtitle1:   user.Department,
		Subtitle2:   null.NewString(fmt.Sprintf("%s %d", user.University.String, user.Batch.Int64), user.University.Valid && user.Batch.Valid),
		Description: string(invitation.Status),
	}

	err = i.notificationUseCase.Create(ctx, &notification)
	if err != nil {
		return err
	}

	return err
}

func (i *invitationUseCase) GetInvitation(ctx context.Context, invitationId int64) (domain.InvitationResponse, error) {
	invitation, err := i.invitationRepository.GetById(ctx, invitationId)
	if err != nil && errors.Is(err, sql.ErrNoRows) {
		return domain.InvitationResponse{}, errors.WithMessage(domain.ErrNotFound, "Invitation not found")
	} else if err != nil {
		return domain.InvitationResponse{}, err
	}

	inviter, err := i.userRepository.GetById(ctx, invitation.InviterId)
	if err != nil && errors.Is(err, sql.ErrNoRows) {
		return domain.InvitationResponse{}, errors.WithMessage(domain.ErrNotFound, "Inviter not found")
	} else if err != nil {
		return domain.InvitationResponse{}, err
	}

	invitee, err := i.userRepository.GetById(ctx, invitation.InviteeId)
	if err != nil && errors.Is(err, sql.ErrNoRows) {
		return domain.InvitationResponse{}, errors.WithMessage(domain.ErrNotFound, "Inviter not found")
	} else if err != nil {
		return domain.InvitationResponse{}, err
	}

	return domain.InvitationResponse{
		ID:          invitation.ID,
		Inviter:     domain.UserToUserResponse(inviter),
		Invitee:     domain.UserToUserResponse(invitee),
		Description: invitation.Description,
		File:        invitation.File,
		Response:    invitation.Response,
		Status:      invitation.Status,
		CreatedAt:   invitation.CreatedAt.UnixMilli(),
	}, nil
}
