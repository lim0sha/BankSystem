CREATE TABLE Users
(
    id        SERIAL PRIMARY KEY,
    login     VARCHAR(255) UNIQUE NOT NULL,
    name      VARCHAR(255)        NOT NULL,
    age       INT                 NOT NULL,
    sex       VARCHAR(255)        NOT NULL,
    hairColor VARCHAR(255)
);

CREATE TABLE BankAccounts
(
    id        SERIAL PRIMARY KEY,
    balance   DOUBLE PRECISION NOT NULL,
    userLogin VARCHAR(255)     NOT NULL,
    userId    INT              NOT NULL REFERENCES Users (id)
);

CREATE TABLE Operations
(
    id        SERIAL PRIMARY KEY,
    accountId INT              NOT NULL REFERENCES BankAccounts (id),
    type      VARCHAR(255)     NOT NULL,
    amount    DOUBLE PRECISION NOT NULL
);

CREATE TABLE Friends
(
    userId   INT NOT NULL REFERENCES Users (id),
    friendId INT NOT NULL REFERENCES Users (id),
    PRIMARY KEY (userId, friendId)
);