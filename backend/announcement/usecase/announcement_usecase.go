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

type announcementUseCase struct {
	announcementRepository domain.AnnouncementRepository
	userRepository         domain.UserRepository
	storageUseCase         domain.StorageUseCase
}

func NewAnnouncementUseCase(announcementRepository domain.AnnouncementRepository, userRepository domain.UserRepository, storageUseCase domain.StorageUseCase) domain.AnnouncementUseCase {
	return &announcementUseCase{announcementRepository, userRepository, storageUseCase}
}

func (a *announcementUseCase) Create(ctx context.Context, req *domain.CreateAnnouncementRequest) (domain.AnnouncementResponse, error) {
	var fileUrl null.String

	if req.File != nil && req.File.Size > 0 {
		storage := domain.Storage{
			File:   req.File,
			Name:   fmt.Sprintf("%d-%d", time.Now().UnixMilli(), req.AuthorId),
			Folder: "announcement",
		}
		url, err := a.storageUseCase.ImageUpload(ctx, &storage)
		if err != nil {
			return domain.AnnouncementResponse{}, err
		}

		fileUrl = null.NewString(url, true)
	}

	announcement := domain.Announcement{
		AuthorId:    req.AuthorId,
		Description: req.Description,
		File:        fileUrl,
		CreatedAt:   time.Now(),
		UpdatedAt:   time.Now(),
	}

	err := a.announcementRepository.Create(ctx, &announcement)
	if err != nil {
		return domain.AnnouncementResponse{}, err
	}

	author, err := a.userRepository.GetById(ctx, announcement.AuthorId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return domain.AnnouncementResponse{}, errors.WithMessage(domain.ErrNotFound, "Author not found")
	} else if err != nil {
		return domain.AnnouncementResponse{}, err
	}

	return domain.AnnouncementResponse{
		ID:          announcement.ID,
		Author:      domain.UserToUserResponse(author),
		Description: announcement.Description,
		File:        announcement.File,
		CreatedAt:   announcement.CreatedAt.UnixMilli(),
	}, err
}

func (a *announcementUseCase) GetAnnouncements(ctx context.Context, authorId int64) ([]domain.AnnouncementResponse, error) {
	announcements, err := a.announcementRepository.Get(ctx, authorId)
	if err != nil {
		return nil, err
	}

	res := make([]domain.AnnouncementResponse, 0)

	for _, announcement := range announcements {
		author, err := a.userRepository.GetById(ctx, announcement.AuthorId)
		if errors.Is(err, sql.ErrNoRows) {
			logrus.Error(err)
			return []domain.AnnouncementResponse{}, errors.WithMessage(domain.ErrNotFound, "Author not found")
		} else if err != nil {
			return []domain.AnnouncementResponse{}, err
		}

		res = append(res, domain.AnnouncementResponse{
			ID:          announcement.ID,
			Author:      domain.UserToUserResponse(author),
			Description: announcement.Description,
			File:        announcement.File,
			CreatedAt:   announcement.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}

func (a *announcementUseCase) GetAnnouncement(ctx context.Context, announcementId int64) (domain.AnnouncementResponse, error) {
	announcement, err := a.announcementRepository.GetById(ctx, announcementId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return domain.AnnouncementResponse{}, errors.WithMessage(domain.ErrNotFound, "Announcement not found")
	} else if err != nil {
		return domain.AnnouncementResponse{}, err
	}

	author, err := a.userRepository.GetById(ctx, announcement.AuthorId)
	if errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return domain.AnnouncementResponse{}, errors.WithMessage(domain.ErrNotFound, "Author not found")
	} else if err != nil {
		return domain.AnnouncementResponse{}, err
	}

	return domain.AnnouncementResponse{
		ID:          announcement.ID,
		Author:      domain.UserToUserResponse(author),
		Description: announcement.Description,
		File:        announcement.File,
		CreatedAt:   announcement.CreatedAt.UnixMilli(),
	}, nil
}
