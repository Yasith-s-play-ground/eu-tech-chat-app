CREATE TABLE users
(
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255)        NOT NULL,
    email    VARCHAR(100) UNIQUE NOT NULL,
    phone    VARCHAR(12) UNIQUE  NOT NULL
);

CREATE TABLE chat_history
(
    id      SERIAL PRIMARY KEY,
    sender  VARCHAR(50) NOT NULL REFERENCES users (username),
    message TEXT        NOT NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE chat_receivers
(
    id      SERIAL PRIMARY KEY,
    chat_id int NOT NULL REFERENCES chat_history(id),
    receiver  VARCHAR(50) NOT NULL REFERENCES users (username)
);
