package hexlet.code.dto.Task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TaskCreateDTO {

//    {
//        "index": 12,
//            "assignee_id": 1,
//            "title": "Test title",
//            "content": "Test content",
//            "status": "draft"
//    }


    @NotBlank
    private String title;
    private Integer index;
    @NotNull
    private Long assignee_id;
    private String content;
    private String status;

}
