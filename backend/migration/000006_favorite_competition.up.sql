CREATE TABLE Favorite_Competition
(
    user_id        BIGINT      NOT NULL REFERENCES App_User (id),
    competition_id BIGINT      NOT NULL REFERENCES Competition (id),
    created_at     timestamptz NOT NULL,
    PRIMARY KEY(user_id, competition_id)
)