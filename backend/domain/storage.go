package domain

import (
	"context"
	"mime/multipart"
)

type Storage struct {
	File   *multipart.FileHeader
	Name   string
	Folder string
}

type StorageRepository interface {
	Upload(ctx context.Context, storage *Storage) (string, error)
}

type StorageUseCase interface {
	ImageUpload(ctx context.Context, storage *Storage) (string, error)
	PdfUpload(ctx context.Context, storage *Storage) (string, error)
}
