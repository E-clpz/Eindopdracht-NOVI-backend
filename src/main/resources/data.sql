DELETE FROM requests;
DELETE FROM users;

ALTER SEQUENCE users_id_seq RESTART WITH 1;

INSERT INTO users (username, email, phone_number, city, password, role)
VALUES ('requester', 'requester@example.com', '0612345678', 'Amsterdam',
        '$2a$10$pVsrHQGv/0Imfu0ueOO0FuROd6f7T84cksaM.udVht6VEk91el1ke', 'REQUESTER');

INSERT INTO users (username, email, phone_number, city, password, role)
VALUES ('helper', 'helper@example.com', '0623456789', 'Rotterdam',
        '$2a$10$pVsrHQGv/0Imfu0ueOO0FuROd6f7T84cksaM.udVht6VEk91el1ke', 'HELPER');

INSERT INTO users (username, email, phone_number, city, password, role)
VALUES ('admin', 'admin@example.com', '0634567890', 'Utrecht',
        '$2a$10$pVsrHQGv/0Imfu0ueOO0FuROd6f7T84cksaM.udVht6VEk91el1ke', 'ADMIN');

INSERT INTO categories (name) VALUES ('Boodschappen');
INSERT INTO categories (name) VALUES ('Vervoer');
INSERT INTO categories (name) VALUES ('Gezelschap');
INSERT INTO categories (name) VALUES ('Overig');

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Boodschappen doen', 'Hulp nodig bij de boodschappen', 1, 'OPEN', 'Amsterdam', 1, '2025-03-01', CURRENT_TIMESTAMP);