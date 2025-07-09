package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
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
