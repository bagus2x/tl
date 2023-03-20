package domain

import (
	"context"
	"mime/multipart"
	"time"
)

type Competition struct {
	ID            int64
	AuthorId      int64
	Poster        string
	Title         string
	Description   string
	Theme         string
	City          string
	Country       string
	Deadline      time.Time
	MinimumFee    int64
	MaximumFee    int64
	Category      string
	Organizer     string
	OrganizerName string
	UrlLink       string
	CreatedAt     time.Time
	UpdatedAt     time.Time
}

type CompetitionResponse struct {
	ID            int64  `json:"id"`
	AuthorId      int64  `json:"authorId"`
	Poster        string `json:"poster"`
	Title         string `json:"title"`
	Description   string `json:"description"`
	Theme         string `json:"theme"`
	City          string `json:"city"`
	Country       string `json:"country"`
	Deadline      int64  `json:"deadline"`
	MinimumFee    int64  `json:"minimumFee"`
	MaximumFee    int64  `json:"maximumFee"`
	Category      string `json:"category"`
	Organizer     string `json:"organizer"`
	OrganizerName string `json:"organizerName"`
	UrlLink       string `json:"urlLink"`
	Favorite      bool   `json:"favorite"`
	CreatedAt     int64  `json:"createdAt"`
}

type CreateCompetitionRequest struct {
	AuthorId      int64                 `json:"authorId"`
	Poster        *multipart.FileHeader `json:"poster"`
	Title         string                `json:"title"`
	Description   string                `json:"description"`
	Theme         string                `json:"theme"`
	City          string                `json:"city"`
	Country       string                `json:"country"`
	Deadline      int64                 `json:"deadline"`
	MinimumFee    int64                 `json:"minimumFee"`
	MaximumFee    int64                 `json:"maximumFee"`
	Category      string                `json:"category"`
	Organizer     string                `json:"organizer"`
	OrganizerName string                `json:"organizerName"`
	UrlLink       string                `json:"urlLink"`
}

type CompetitionRepository interface {
	Create(ctx context.Context, competition *Competition) error
	Get(ctx context.Context) ([]Competition, error)
	GetById(ctx context.Context, competitionId int64) (Competition, error)
}

type CompetitionUseCase interface {
	Create(ctx context.Context, competition *CreateCompetitionRequest) (CompetitionResponse, error)
	GetCompetitions(ctx context.Context) ([]CompetitionResponse, error)
	GetCompetition(ctx context.Context, competitionId int64) (CompetitionResponse, error)
}
