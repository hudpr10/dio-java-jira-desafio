--liquibase formatted sql
--changeset hudson:202507241845
--comment: cards table cards_blocks

CREATE TABLE cards_blocks(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    blocked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    blocked_reason VARCHAR(255) NOT NULL,
    unblocked_at TIMESTAMP NULL,
    unblocked_reason VARCHAR(255) NULL,
    card_id BIGINT NOT NULL,

    CONSTRAINT fk_card_id__cards FOREIGN KEY (card_id) REFERENCES cards(id) ON DELETE CASCADE
) ENGINE=InnoDB;

--rollback DROP TABLE cards_blocks
