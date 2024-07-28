-- Create Roles Table
CREATE TABLE IF NOT EXISTS roles (
                                     id UUID PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL
    );

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    token VARCHAR(512),
    created TIMESTAMP,
    modified TIMESTAMP,
    last_login TIMESTAMP,
    active BOOLEAN,
    role_id UUID,
    FOREIGN KEY (role_id) REFERENCES roles(id)
    );

-- Create Phones Table
CREATE TABLE IF NOT EXISTS phones (
                                      id UUID PRIMARY KEY,
                                      number VARCHAR(255) NOT NULL,
    citycode VARCHAR(255) NOT NULL,
    countrycode VARCHAR(255) NOT NULL,
    user_id UUID,
    FOREIGN KEY (user_id) REFERENCES users(id)
    );

-- Sample data for roles
INSERT INTO roles (id, name) VALUES ('123e4567-e89b-12d3-a456-426614174000', 'ROLE_USER');
INSERT INTO roles (id, name) VALUES ('123e4567-e89b-12d3-a456-426614174001', 'ROLE_ADMIN');

-- Sample data for users
INSERT INTO users (id, name, username, email, password, token, created, modified, last_login, active, role_id)
VALUES ('123e4567-e89b-12d3-a456-426614174002', 'John Doe', 'johndoe', 'johndoe@example.com', 'encryptedPassword', 'sampleToken', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, TRUE, '123e4567-e89b-12d3-a456-426614174000');

-- Sample data for phones
INSERT INTO phones (id, number, citycode, countrycode, user_id)
VALUES ('123e4567-e89b-12d3-a456-426614174003', '123456789', '1', '57', '123e4567-e89b-12d3-a456-426614174002');
