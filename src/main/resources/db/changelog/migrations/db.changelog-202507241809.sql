--liquibase formatted sql
--changeset hudson:202507241809
--comment: boards table create

CREATE TABLE boards(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL

) ENGINE=InnoDB;

--rollback DROP TABLE boards
