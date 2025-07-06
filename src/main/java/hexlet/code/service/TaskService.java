package hexlet.code.service;


import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;

    public TaskDTO getTaskById(Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task not found by id= " + id));
        return taskMapper.map(task);
    }

    public List<TaskDTO> getTasks(TaskParamsDTO params) {
        var spec = taskSpecification.build(params);
        var tasks = taskRepository.findAll(spec);
        var mappedTasks = tasks.stream()
                .map(taskMapper::map)
                .toList();

        return mappedTasks;
    }

    public TaskDTO createTask(TaskCreateDTO createDTO) {
        Task taskToAdd = taskMapper.map(createDTO);
        taskRepository.save(taskToAdd);
        return taskMapper.map(taskToAdd);
    }

    public TaskDTO update(TaskUpdateDTO updateDTO, Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task not found by id= " + id));
        taskMapper.update(task, updateDTO);
        taskRepository.save(task);

        return taskMapper.map(task);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

}
