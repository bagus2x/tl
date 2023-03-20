package usecase

import (
	"bagus2x/temanlomba/domain"
	"context"
)

type storageUseCase struct {
	storageRepository domain.StorageRepository
}

func NewStorageUseCase(storageRepository domain.StorageRepository) domain.StorageUseCase {
	return &storageUseCase{storageRepository}
}

func (s *storageUseCase) ImageUpload(ctx context.Context, storage *domain.Storage) (string, error) {
	return s.storageRepository.Upload(ctx, storage)
}

func (s *storageUseCase) PdfUpload(ctx context.Context, storage *domain.Storage) (string, error) {
	//TODO implement me
	panic("implement me")
}
