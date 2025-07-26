package dio.persistence.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardEntity {

    private long id;
    private String name;
    @EqualsAndHashCode.Exclude
    private List<BoardColumnEntity> boardsColumns = new ArrayList<>();

    public BoardColumnEntity getInitialColumn() {
        return boardsColumns.stream()
                .filter(bc -> bc.getKind().equals(BoardColumnKindEnum.INITIAL))
                .findFirst()
                .orElseThrow();
    }
}
