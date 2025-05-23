INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');


INSERT INTO users (email, username, name, role_id)
VALUES ('admin@example.com', 'admin', 'ADMIN', (SELECT id FROM roles WHERE name = 'ADMIN'));