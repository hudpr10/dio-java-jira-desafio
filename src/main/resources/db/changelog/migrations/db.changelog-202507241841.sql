--liquibase formatted sql
--changeset hudson:202507241841
--comment: cards table create

CREATE TABLE cards(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    card_order INT NOT NULL,
    board_column_id BIGINT NOT NULL,

    CONSTRAINT fk_board_column_id__boards_columns FOREIGN KEY (board_column_id) REFERENCES boards_columns(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE cards
