package hexlet.code.controller;

import hexlet.code.Model.TaskStatus;
import hexlet.code.dto.TaskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatus.TaskStatusDTO;
import hexlet.code.dto.TaskStatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.repository.TaskStatusRepository;
import jakarta.validation.Valid;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
public class TaskStatusController {

    @Autowired
    private TaskStatusMapper taskStatusMapper;
    @Autowired
    private TaskStatusRepository taskStatusRepository;


    @GetMapping("/{id}")
    public TaskStatusDTO getStatusByID(@PathVariable Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found by " + id));

        return taskStatusMapper.map(status);
    }

    @GetMapping("")
    public ResponseEntity<List<TaskStatusDTO>> getTaskStatuses() {
        var statuses = taskStatusRepository.findAll();
        List<TaskStatusDTO> list = new ArrayList<>();

        statuses.forEach(s -> list.add(taskStatusMapper.map(s)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(list.size()))
                .body(list);

    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO createUser(@Valid @RequestBody TaskStatusCreateDTO createDTO) {
        TaskStatus taskStatus = taskStatusMapper.map(createDTO);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);

    }

    @PutMapping("/{id}")
    public TaskStatusDTO update(@RequestBody TaskStatusUpdateDTO updateDTO, @PathVariable Long id) {
        if (!taskStatusRepository.existsById(id)) {
            throw new ResourceNotFoundException("not found by id= " + id);
        }
        TaskStatus task = taskStatusRepository.getById(id);
        taskStatusMapper.update(updateDTO, task);
        taskStatusRepository.save(task);

        return taskStatusMapper.map(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskStatusRepository.deleteById(id);
        return;
    }
}
