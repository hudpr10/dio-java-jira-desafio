package dio.persistence.entity;

import lombok.Data;

@Data
public class BoardColumnEntity {

    private long id;
    private String name;
    private int order;
    private BoardColumnKindEnum kind;
    private BoardEntity boardEntity = new BoardEntity();

}
