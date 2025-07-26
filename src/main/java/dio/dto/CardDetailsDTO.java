package dio.dto;

import java.time.OffsetDateTime;

public record CardDetailsDTO(
        long id,
        String title,
        String description,
        boolean blocked,
        OffsetDateTime blockedAt,
        String blockedReason,
        int blocksAmount,
        long columnId,
        String columnName) {
}
