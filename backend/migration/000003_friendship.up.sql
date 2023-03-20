CREATE TABLE Friendship
(
    requester_id BIGSERIAL REFERENCES App_User (id),
    addressee_id BIGSERIAL REFERENCES App_User (id),
    accepted     BOOLEAN,
    created_at   timestamptz NOT NULL DEFAULT now(),
    CONSTRAINT friendship_pk PRIMARY KEY (requester_id, addressee_id)
)