package hexlet.code.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Initialization {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ObjectMapper objectMapper; // Jackson JSON parser

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Reads users from a JSON file and saves them to the database.
     *
     * @param filePath Path to the JSON file (e.g., "src/main/resources/users.json")
     */
    public void initUsersFromJsonFile(String filePath) throws IOException {
        String jsonContent = Files.readString(Paths.get(filePath));
        List<UserCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, UserCreateDTO[].class));

        List<User> list = new ArrayList<>();
        for (UserCreateDTO dto : users) {
            User user = userMapper.map(dto);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            list.add(user);
        }

        userRepository.saveAll(list);
    }


    public void initTaskStatusesFromJsonFile(String filePath) throws IOException {

        String jsonContent = Files.readString(Paths.get(filePath));
        List<TaskStatusCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, TaskStatusCreateDTO[].class));

        List<TaskStatus> list = new ArrayList<>();

        users.forEach(user -> list.add(taskStatusMapper.map(user)));

        taskStatusRepository.saveAll(list);
    }

    public void initTasksFromJsonFile(String filePath) throws IOException {

        String jsonContent = Files.readString(Paths.get(filePath));
        List<TaskCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, TaskCreateDTO[].class));

        List<Task> list = new ArrayList<>();

        users.forEach(task -> list.add(taskMapper.map(task)));

        list.forEach(task -> {
            var user = userRepository.findAll().stream().findFirst().orElseThrow(() ->
                    new ResourceNotFoundException("user not found"));
            task.setAssignee(user);
        });

        taskRepository.saveAll(list);
    }

    /**
     * Reads tasks from a string in json format and saves them to the database.
     *
     * @param string tasks in json format
     */
    public void initTasksFromString(String string) throws IOException {


        // System.out.println("json text before mapping: "+jsonContent);
        List<TaskCreateDTO> users = Arrays.asList(objectMapper.readValue(string, TaskCreateDTO[].class));

        users.forEach(user -> System.out.println(user.toString()));
        List<Task> list = new ArrayList<>();
        users.forEach(user -> list.add(taskMapper.map(user)));

//        Debug
//        users.forEach(u -> {
//            System.out.println("DTO: " + u.getEmail() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getPassword());
//        });

//        list.forEach(u -> {
//            System.out.println("ENTITY: " + u.getEmail() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getPassword());
//        });


        taskRepository.saveAll(list);
    }

    public void initLabelsFromJsonFile(String filePath) throws IOException {

        String jsonContent = Files.readString(Paths.get(filePath));
        List<LabelCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, LabelCreateDTO[].class));

        List<Label> list = new ArrayList<>();

        users.forEach(label -> list.add(labelMapper.map(label)));

        labelRepository.saveAll(list);
    }


}