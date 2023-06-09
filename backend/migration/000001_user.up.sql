CREATE TABLE App_User
(
    id                BIGSERIAL PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL UNIQUE,
    verified          BOOLEAN      NOT NULL,
    verification_code VARCHAR(6)   NOT NULL,
    password          VARCHAR(512) NOT NULL,
    photo             VARCHAR(512),
    university        VARCHAR(255),
    faculty           VARCHAR(255),
    department        VARCHAR(255),
    studyProgram      VARCHAR(255),
    stream            VARCHAR(255),
    batch             INT,
    gender            VARCHAR(127),
    age               INT,
    bio               VARCHAR(1024),
    achievements      VARCHAR(512)[],
    certifications    VARCHAR(512)[],
    skills            VARCHAR(512)[],
    invitable         BOOLEAN      NOT NULL,
    location          VARCHAR(512) NULL,
    total_rating      NUMERIC      NOT NULL,
    votes             INT          NOT NULL,
    likes             INT          NOT NULL CHECK ( likes >= 0 ),
    friends           INT          NOT NULL CHECK ( friends >= 0 ),
    created_at        TIMESTAMPTZ  NOT NULL,
    updated_at        TIMESTAMPTZ  NOT NULL
);