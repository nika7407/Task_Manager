package hexlet.code.mapper;

import hexlet.code.Model.Task;
import hexlet.code.Model.TaskStatus;
import hexlet.code.Model.User;
import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.Task.TaskDTO;
import hexlet.code.dto.Task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Mapping(source = "title", target = "name")
    @Mapping(source = "assignee_id", target = "assignee", qualifiedByName = "idToAssignee")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "slugToTaskStatus")
    public abstract Task map(TaskCreateDTO createDTO);

    @Mapping(source = "name", target = "title")
    @Mapping(source = "assignee", target = "assignee_id", qualifiedByName = "assigneeToId")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "taskStatus", target = "status", qualifiedByName = "taskToSlug")
    public abstract TaskDTO map(Task task);

    @Named("slugToTaskStatus")
    public TaskStatus slugToTaskStatus(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with slug '" + slug + "' not found"));
    }

    @Named("taskToSlug")
    public String  taskToSlug(TaskStatus task) {
     return task.getSlug();
    }

    @Named("idToAssignee")
    public User idToAssignee(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return userRepository.findById(assigneeId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id '" + assigneeId + "' not found"));
    }
    @Named("assigneeToId")
    public Long  assigneeToId(User assignee) {
        if (assignee == null) {
            return null;
        }
        return assignee.getId();
    }
    @Mapping(source = "title", target = "name")
    @Mapping(source = "assignee_id", target = "assignee", qualifiedByName = "idToAssignee")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus", qualifiedByName = "slugToTaskStatus")
    public abstract void update(@MappingTarget Task task, TaskUpdateDTO updateDTO);
}
