package hexlet.code.Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Model.Task;
import hexlet.code.Model.TaskStatus;
import hexlet.code.Model.User;
import hexlet.code.dto.Task.TaskCreateDTO;
import hexlet.code.dto.TaskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatus.TaskStatusDTO;
import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * Reads users from a JSON file and saves them to the database.
     *
     * @param filePath Path to the JSON file (e.g., "src/main/resources/users.json")
     */
    public void initUsersFromJsonFile(String filePath) throws IOException {

        String jsonContent = Files.readString(Paths.get(filePath));
     // System.out.println("json text before mapping: "+jsonContent);
        List<UserCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, UserCreateDTO[].class));

        users.forEach(user -> System.out.println(user.toString()));
        List<User> list = new ArrayList<>();
        users.forEach(user -> list.add(userMapper.map(user)));

//        users.forEach(u -> {
//            System.out.println("DTO: " + u.getEmail() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getPassword());
//        });


//        list.forEach(u -> {
//            System.out.println("ENTITY: " + u.getEmail() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getPassword());
//        });


        userRepository.saveAll(list);
    }

    public void initTaskStatusesFromJsonFile(String filePath) throws IOException {

        String jsonContent = Files.readString(Paths.get(filePath));
        List<TaskStatusCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, TaskStatusCreateDTO[].class));

        List<TaskStatus> list = new ArrayList<>();

        users.forEach(user-> list.add(taskStatusMapper.map(user)));

        taskStatusRepository.saveAll(list);
    }

    public void initTasksFromJsonFile(String filePath) throws IOException {

        String jsonContent = Files.readString(Paths.get(filePath));
        List<TaskCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, TaskCreateDTO[].class));

        List<Task> list = new ArrayList<>();

        users.forEach(task-> list.add(taskMapper.map(task)));

        taskRepository.saveAll(list);
    }
}