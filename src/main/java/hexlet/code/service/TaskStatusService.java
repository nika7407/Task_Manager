package hexlet.code.service;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusService {
    private final TaskStatusMapper taskStatusMapper;
    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusDTO getStatusByID(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found by " + id));

        return taskStatusMapper.map(status);
    }

    public List<TaskStatusDTO> getTaskStatuses() {
        var statuses = taskStatusRepository.findAll();
        List<TaskStatusDTO> list = new ArrayList<>();

        statuses.forEach(s -> list.add(taskStatusMapper.map(s)));
        return list;
    }

    public TaskStatusDTO createUser(TaskStatusCreateDTO createDTO) {
        TaskStatus taskStatus = taskStatusMapper.map(createDTO);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);

    }

    public TaskStatusDTO update(TaskStatusUpdateDTO updateDTO, Long id) {

        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("not found by " + id));

        taskStatusMapper.update(updateDTO, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public void delete(Long id) {
        taskStatusRepository.deleteById(id);
    }

}
