package repository

import (
	"bagus2x/temanlomba/domain"
	"context"
	"github.com/cloudinary/cloudinary-go"
	"github.com/cloudinary/cloudinary-go/api/uploader"
	"github.com/sirupsen/logrus"
	"mime/multipart"
)

type cloudinaryStorageRepository struct {
	cld *cloudinary.Cloudinary
}

func NewCloudinaryStorageRepository(cloudName string, apiKey string, apiSecret string) domain.StorageRepository {
	cld, err := cloudinary.NewFromParams(cloudName, apiKey, apiSecret)
	if err != nil {
		panic(err)
	}
	return &cloudinaryStorageRepository{cld}
}

func (c *cloudinaryStorageRepository) Upload(ctx context.Context, storage *domain.Storage) (string, error) {
	file, err := storage.File.Open()
	defer func(file multipart.File) {
		err := file.Close()
		if err != nil {
			logrus.Error(err)
		}
	}(file)

	if err != nil {
		logrus.Error(err)
		return "", err
	}

	uploaded, err := c.cld.Upload.Upload(ctx, file, uploader.UploadParams{
		Folder:   storage.Folder,
		PublicID: storage.Name,
	})
	if err != nil {
		logrus.Error(err)
		return "", err
	}
	return uploaded.SecureURL, nil
}
