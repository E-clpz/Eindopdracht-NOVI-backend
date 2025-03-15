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

INSERT INTO requests (title, description, category_id, status, city, requester_id, preferred_date, helper_id)
VALUES
    ('Hulp bij boodschappen', 'Boodschappen doen voor een oudere persoon', 1, 'GEACCEPTEERD', 'Amsterdam', 1, '2025-01-01', 2),
    ('Vervoer naar dokter', 'Hulp nodig voor vervoer naar de dokter', 2, 'Open', 'Rotterdam', 1, '2025-02-01', NULL),
    ('Vervoer naar ziekenhuis', 'Ik heb een lift nodig naar het ziekenhuis', 3, 'Open', 'Utrecht', 1, '2025-03-10', NULL),
    ('Gezelschap voor wandeling', 'Ik zoek iemand voor een gezellige wandeling in het park', 3, 'Open', 'Groningen', 1, '2025-04-01', NULL),
    ('Boodschappen doen voor ouderen', 'Ik help graag ouderen met hun wekelijkse boodschappen', 1, 'Open', 'Leiden', 1, '2025-04-05', NULL),
    ('Vervoer naar ziekenhuis', 'Ik heb iemand nodig die me naar het ziekenhuis kan brengen', 2, 'Open', 'Den Haag', 1, '2025-04-10', NULL),
    ('Hulp met tuinieren', 'Ik zoek hulp voor tuinonderhoud, zoals gras maaien en bloemen planten', 4, 'Open', 'Maastricht', 1, '2025-04-15', NULL),
    ('Gezelschap bij een film', 'Ik wil graag iemand uitnodigen om samen een film te kijken', 3, 'Open', 'Eindhoven', 1, '2025-04-20', NULL);
