package hexlet.code.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Util {

    private final UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper; // Jackson JSON parser

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    public Util(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public String changeID(String originalTasks) {
//        Long id = userRepository.findAll().getFirst().getId();
//        return originalTasks.replaceAll("TOCHANGE", String.valueOf(id));
//    }


    public String setTasksAsigneeId(String taskInJson) throws JsonProcessingException {
        TaskCreateDTO dto = objectMapper.readValue(taskInJson, TaskCreateDTO.class);

        dto.setAssignee_id(userRepository.findAll().stream().findFirst().orElseThrow(() ->
                new ResourceNotFoundException("no users to assign")).getId());
        return objectMapper.writeValueAsString(dto);
    }
}

