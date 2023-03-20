CREATE TABLE Testimony
(
    id          BIGSERIAL PRIMARY KEY,
    receiver_id BIGINT REFERENCES App_User (id),
    sender_id   BIGINT REFERENCES App_User (id),
    description VARCHAR(1024) NOT NULL,
    rating      NUMERIC       NOT NULL,
    created_at  timestamptz   NOT NULL,
    updated_at  timestamptz   NOT NULL
)