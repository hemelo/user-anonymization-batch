CREATE TABLE users (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       name VARCHAR(255),
       email VARCHAR(255),
       phone VARCHAR(20),
       address VARCHAR(255),
       anonymized BOOLEAN DEFAULT FALSE
);