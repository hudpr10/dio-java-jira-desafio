package dio.persistence.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardEntity {

    private long id;
    private String name;
    private List<BoardColumnEntity> boardsColumns = new ArrayList<>();

}
