package hexlet.code.controller;


import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;


    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task not found by id= " + id));
        return taskMapper.map(task);
    }

    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> getTasks(TaskParamsDTO params) {
        var spec = taskSpecification.build(params);
        var tasks = taskRepository.findAll(spec);
        var mappedTasks = tasks.stream()
                .map(taskMapper::map)
                .toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(mappedTasks.size()))
                .body(mappedTasks);
    }


    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO createTask(@Valid @RequestBody TaskCreateDTO createDTO) {
        Task taskToAdd = taskMapper.map(createDTO);
        taskRepository.save(taskToAdd);
        return taskMapper.map(taskToAdd);

    }

    @PutMapping("/{id}")
    public TaskDTO update(@RequestBody TaskUpdateDTO updateDTO, @PathVariable Long id) {
        var task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("task not found by id= " + id));
        taskMapper.update(task, updateDTO);
        taskRepository.save(task);

        return taskMapper.map(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return;
    }
}
