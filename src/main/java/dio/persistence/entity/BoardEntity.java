package dio.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Data
public class BoardEntity {

    private long id;
    private String name;
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardsColumns = new ArrayList<>();

    public BoardColumnEntity getInitialColumn() {
        return getFilteredColumn(column -> column.getKind().equals(BoardColumnKindEnum.FINAL));
    }

    public BoardColumnEntity getCancelColumn() {
        return getFilteredColumn(column -> column.getKind().equals(BoardColumnKindEnum.CANCELED));
    }

    private BoardColumnEntity getFilteredColumn(Predicate<BoardColumnEntity> filter) {
        return boardsColumns.stream()
                .filter(filter)
                .findFirst()
                .orElseThrow();
    }
}
