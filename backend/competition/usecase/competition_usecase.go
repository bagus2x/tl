package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
	"database/sql"
	"fmt"
	"github.com/pkg/errors"
	"github.com/sirupsen/logrus"
	"time"
)

type competitionUseCase struct {
	competitionRepository         domain.CompetitionRepository
	userUseCase                   domain.UserUseCase
	storageUseCase                domain.StorageUseCase
	favoriteCompetitionRepository domain.FavCompetitionRepository
}

func NewCompetitionUseCase(
	competitionRepository domain.CompetitionRepository,
	userUseCase domain.UserUseCase,
	storageUseCase domain.StorageUseCase,
	favoriteCompetitionRepository domain.FavCompetitionRepository,
) domain.CompetitionUseCase {
	return &competitionUseCase{
		competitionRepository,
		userUseCase, storageUseCase,
		favoriteCompetitionRepository,
	}
}

func (c *competitionUseCase) Create(ctx context.Context, req *domain.CreateCompetitionRequest) (domain.CompetitionResponse, error) {
	storage := domain.Storage{
		File:   req.Poster,
		Name:   fmt.Sprintf("%d-%d", time.Now().UnixMilli(), req.AuthorId),
		Folder: "poster",
	}
	posterUrl, err := c.storageUseCase.ImageUpload(ctx, &storage)
	if err != nil {
		return domain.CompetitionResponse{}, err
	}

	competition := domain.Competition{
		AuthorId:      req.AuthorId,
		Poster:        posterUrl,
		Title:         req.Title,
		Description:   req.Description,
		Theme:         req.Theme,
		City:          req.City,
		Country:       req.Country,
		Deadline:      time.UnixMilli(req.Deadline),
		MinimumFee:    req.MinimumFee,
		MaximumFee:    req.MaximumFee,
		Category:      req.Category,
		Organizer:     req.Organizer,
		OrganizerName: req.OrganizerName,
		UrlLink:       req.UrlLink,
		CreatedAt:     time.Now(),
		UpdatedAt:     time.Now(),
	}
	if err := c.competitionRepository.Create(ctx, &competition); err != nil {
		logrus.Error(err)
		return domain.CompetitionResponse{}, err
	}

	return domain.CompetitionResponse{
		ID:            competition.ID,
		AuthorId:      competition.AuthorId,
		Poster:        competition.Poster,
		Title:         competition.Title,
		Description:   competition.Description,
		Theme:         competition.Theme,
		City:          competition.City,
		Country:       competition.Country,
		Deadline:      competition.Deadline.UnixMilli(),
		MinimumFee:    competition.MinimumFee,
		MaximumFee:    competition.MaximumFee,
		Category:      competition.Category,
		Organizer:     competition.Organizer,
		OrganizerName: competition.OrganizerName,
		UrlLink:       competition.UrlLink,
		CreatedAt:     competition.CreatedAt.UnixMilli(),
	}, nil
}

func (c *competitionUseCase) GetCompetitions(ctx context.Context) ([]domain.CompetitionResponse, error) {
	competitions, err := c.competitionRepository.Get(ctx)
	if err != nil {
		return nil, err
	}

	var res = make([]domain.CompetitionResponse, 0)

	for _, competition := range competitions {

		isFavorites, err := c.IsFavCompetition(ctx, competition.ID)
		if err != nil {
			return nil, err
		}

		res = append(res, domain.CompetitionResponse{
			ID:            competition.ID,
			AuthorId:      competition.AuthorId,
			Poster:        competition.Poster,
			Title:         competition.Title,
			Description:   competition.Description,
			Theme:         competition.Theme,
			City:          competition.City,
			Country:       competition.Country,
			Deadline:      competition.Deadline.UnixMilli(),
			MinimumFee:    competition.MinimumFee,
			MaximumFee:    competition.MaximumFee,
			Category:      competition.Category,
			Organizer:     competition.Organizer,
			OrganizerName: competition.OrganizerName,
			UrlLink:       competition.UrlLink,
			Favorite:      isFavorites,
			CreatedAt:     competition.CreatedAt.UnixMilli(),
		})
	}

	return res, nil
}

func (c *competitionUseCase) GetCompetition(ctx context.Context, competitionId int64) (domain.CompetitionResponse, error) {
	competition, err := c.competitionRepository.GetById(ctx, competitionId)
	if err != nil && errors.Is(err, sql.ErrNoRows) {
		logrus.Error(err)
		return domain.CompetitionResponse{}, errors.WithMessage(domain.ErrNotFound, "Competition not found")
	} else if err != nil {
		return domain.CompetitionResponse{}, err
	}

	isFavorites, err := c.IsFavCompetition(ctx, competition.ID)
	if err != nil {
		return domain.CompetitionResponse{}, err
	}

	return domain.CompetitionResponse{
		ID:            competition.ID,
		AuthorId:      competition.AuthorId,
		Poster:        competition.Poster,
		Title:         competition.Title,
		Description:   competition.Description,
		Theme:         competition.Theme,
		City:          competition.City,
		Country:       competition.Country,
		Deadline:      competition.Deadline.UnixMilli(),
		MinimumFee:    competition.MinimumFee,
		MaximumFee:    competition.MaximumFee,
		Category:      competition.Category,
		Organizer:     competition.Organizer,
		OrganizerName: competition.OrganizerName,
		UrlLink:       competition.UrlLink,
		Favorite:      isFavorites,
		CreatedAt:     competition.CreatedAt.UnixMilli(),
	}, nil
}

func (c *competitionUseCase) IsFavCompetition(ctx context.Context, competitionId int64) (bool, error) {
	userId, _ := ctx.Value("userId").(int64)
	fav, err := c.favoriteCompetitionRepository.GetByUserIdAndCompId(ctx, userId, competitionId)
	if err != nil && !errors.Is(err, sql.ErrNoRows) {
		return false, err
	}

	return fav != (domain.FavCompetition{}), nil
}
