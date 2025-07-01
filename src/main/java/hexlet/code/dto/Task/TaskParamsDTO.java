package hexlet.code.dto.Task;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskParamsDTO {

//    titleCont - название задачи содержит подстроку
//    assigneeId - идентификатор исполнителя задачи
//    status - слаг статуса задачи
//    labelId - идентификатор метки задачи

    private String titleCont;

    private Long assigneeId;

    private  String status;

    private  Long labelId;
}
