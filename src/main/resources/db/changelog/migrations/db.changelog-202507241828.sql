--liquibase formatted sql
--changeset hudson:202507241828
--comment: boards_columns table create

CREATE TABLE boards_columns(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    column_order INT NOT NULL,
    kind VARCHAR(8) NOT NULL,
    board_id BIGINT NOT NULL,

    CONSTRAINT fk_boards__boards_columns FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE,
    CONSTRAINT uk_column_order UNIQUE KEY unique_board_id_order(board_id, column_order)
) ENGINE=InnoDB;

--rollback DROP TABLE boards_columns
