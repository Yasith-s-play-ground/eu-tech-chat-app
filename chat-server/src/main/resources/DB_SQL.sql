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

-- SELECT ch.id AS message_id, ch.sender, ch.message, ch.sent_at, NULL AS receiver FROM chat_history ch WHERE ch.sender = 'yasithperera' UNION ALL SELECT cr.chat_id AS message_id, ch.sender, ch.message, ch.sent_at, cr.receiver FROM chat_receivers cr JOIN chat_history ch ON cr.chat_id = ch.id WHERE cr.receiver = 'yasithperera' ORDER BY sent_at

SELECT ch.id AS chat_id, ch.sender, cr.receiver, ch.message, ch.sent_at FROM chat_history ch JOIN chat_receivers cr ON ch.id = cr.chat_id WHERE (ch.sender = 'yasithperera' AND cr.receiver = 'madhu') OR (ch.sender = 'madhu' AND cr.receiver = 'yasithperera') ORDER BY ch.sent_at;
