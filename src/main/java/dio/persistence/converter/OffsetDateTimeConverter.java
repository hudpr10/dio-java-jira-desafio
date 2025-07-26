package dio.persistence.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OffsetDateTimeConverter {

    public static OffsetDateTime toOffsetDateTime(final Timestamp value) {
        return value != null ? OffsetDateTime.ofInstant(value.toInstant(), ZoneOffset.UTC) : null;
    }

}
