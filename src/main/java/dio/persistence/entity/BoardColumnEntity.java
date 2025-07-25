package dio.persistence.entity;

import lombok.Data;
import lombok.ToString;

@Data
public class BoardColumnEntity {

    private long id;
    private String name;
    private int order;
    private BoardColumnKindEnum kind;
    @ToString.Exclude
    private BoardEntity boardEntity = new BoardEntity();

}
