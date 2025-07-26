package dio.dto;

import dio.persistence.entity.BoardColumnKindEnum;

public record BoardColumnIdOrderKindDTO(long id, int columnOrder, BoardColumnKindEnum kind) {
}
