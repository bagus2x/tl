package repository

import (
	"bagus2x/temanlomba/domain"
	"fmt"
	"github.com/dgraph-io/badger/v3"
	"github.com/sirupsen/logrus"
	"time"
)

type badgerAuthRepository struct {
	db *badger.DB
}

func NewBadgerAuthRepository(db *badger.DB) domain.AuthRepository {
	return &badgerAuthRepository{db}
}

func (b *badgerAuthRepository) Save(auth domain.Auth, duration time.Duration) error {
	return b.db.Update(func(txn *badger.Txn) error {
		e := badger.NewEntry([]byte(fmt.Sprintf("%d", auth.ID)), []byte(auth.Token)).WithTTL(duration)
		return txn.SetEntry(e)
	})
}

func (b *badgerAuthRepository) GetById(id int64) (domain.Auth, error) {
	var token []byte
	err := b.db.View(func(txn *badger.Txn) error {
		item, err := txn.Get([]byte(fmt.Sprintf("%d", id)))
		if err != nil {
			return err
		}

		token, err = item.ValueCopy(nil)
		return err
	})
	if err == badger.ErrKeyNotFound {
		logrus.Error(err)
		return domain.Auth{}, domain.ErrForbidden
	}

	return domain.Auth{
		ID:    id,
		Token: string(token),
	}, err
}

func (b *badgerAuthRepository) Delete(id int64) error {
	return b.db.Update(func(txn *badger.Txn) error {
		return txn.Delete([]byte(fmt.Sprintf("%d", id)))
	})
}
