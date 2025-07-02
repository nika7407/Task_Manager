package hexlet.code.dto.Task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

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
    private Long assignee_id;
    private String content;
    @NotBlank
    private String status;
    private Set<Long> taskLabelIds = new HashSet<>();

}
