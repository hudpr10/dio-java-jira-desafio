package dio.dto;

import dio.persistence.entity.BoardColumnKindEnum;

public record BoardColumnDTO(long id, String name, BoardColumnKindEnum kind, int cardsAmount) {
}
