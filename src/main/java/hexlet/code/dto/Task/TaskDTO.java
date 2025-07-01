package hexlet.code.dto.Task;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class TaskDTO {

//        "id": 1,
//            "index": 3140,
//            "createdAt": "2023-07-30",
//            "assignee_id": 1,
//            "title": "Task 1",
//            "content": "Description of task 1",
//            "status": "to_be_fixed"

    private Long id;
    private Integer index;
    private LocalDate createdAt;
    private Long assignee_id;
    private String title;
    private String content;
    private String status;
    private Set<Long> taskLabelIds = new HashSet<>();

}
