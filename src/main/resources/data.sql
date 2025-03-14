DELETE FROM requests;
DELETE FROM users;

ALTER TABLE file_document
DROP CONSTRAINT fk5n235o55u3k4rydwb5t5sl5am;

ALTER TABLE file_document
    ADD CONSTRAINT fk5n235o55u3k4rydwb5t5sl5am
        FOREIGN KEY (request_id) REFERENCES requests(id)
            ON DELETE CASCADE;

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

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at, helper_id)
VALUES ('Hulp bij boodschappen', 'Boodschappen doen voor een oudere persoon', 1, 'GEACCEPTEERD', 'Amsterdam', 1, '2025-01-01', CURRENT_TIMESTAMP, 2);

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Vervoer naar dokter', 'Hulp nodig voor vervoer naar de dokter', 2, 'Open', 'Rotterdam', 1, '2025-02-01', CURRENT_TIMESTAMP);

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Vervoer naar ziekenhuis', 'Ik heb een lift nodig naar het ziekenhuis', 3, 'Open', 'Utrecht', 1, '2025-03-10', CURRENT_TIMESTAMP);

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Gezelschap voor wandeling', 'Ik zoek iemand voor een gezellige wandeling in het park', 3, 'Open', 'Groningen', 1, '2025-04-01', CURRENT_TIMESTAMP);

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Boodschappen doen voor ouderen', 'Ik help graag ouderen met hun wekelijkse boodschappen', 1, 'Open', 'Leiden', 1, '2025-04-05', CURRENT_TIMESTAMP);

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Vervoer naar ziekenhuis', 'Ik heb iemand nodig die me naar het ziekenhuis kan brengen', 2, 'Open', 'Den Haag', 1, '2025-04-10', CURRENT_TIMESTAMP);

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Hulp met tuinieren', 'Ik zoek hulp voor tuinonderhoud, zoals gras maaien en bloemen planten', 4, 'Open', 'Maastricht', 1, '2025-04-15', CURRENT_TIMESTAMP);

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, created_at)
VALUES ('Gezelschap bij een film', 'Ik wil graag iemand uitnodigen om samen een film te kijken', 3, 'Open', 'Eindhoven', 1, '2025-04-20', CURRENT_TIMESTAMP);