package dio.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(long id, boolean blocked, OffsetDateTime blockedAt, String blockedReason, int blocksAmount, long columnId, String columnName) {
}
