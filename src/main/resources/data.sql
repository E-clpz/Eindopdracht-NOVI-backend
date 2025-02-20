DELETE FROM users;

INSERT INTO users (username, email, phone_number, city, password, role)
VALUES ('requester', 'requester@example.com', '0612345678', 'Amsterdam',
        '$2a$10$pVsrHQGv/0Imfu0ueOO0FuROd6f7T84cksaM.udVht6VEk91el1ke', 'REQUESTER');

INSERT INTO users (username, email, phone_number, city, password, role)
VALUES ('helper', 'helper@example.com', '0623456789', 'Rotterdam',
        '$2a$10$pVsrHQGv/0Imfu0ueOO0FuROd6f7T84cksaM.udVht6VEk91el1ke', 'HELPER');

INSERT INTO users (username, email, phone_number, city, password, role)
VALUES ('admin', 'admin@example.com', '0634567890', 'Utrecht',
        '$2a$10$pVsrHQGv/0Imfu0ueOO0FuROd6f7T84cksaM.udVht6VEk91el1ke', 'ADMIN');
