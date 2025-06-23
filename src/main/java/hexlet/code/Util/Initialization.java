package hexlet.code.Util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.Model.User;
import hexlet.code.dto.UserCreateDTO;
import hexlet.code.mapper.UserMapper;
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
    private ObjectMapper objectMapper; // Jackson JSON parser

    @Autowired
    private UserMapper userMapper;

    /**
     * Reads users from a JSON file and saves them to the database.
     *
     * @param filePath Path to the JSON file (e.g., "src/main/resources/users.json")
     */
    public void initUsersFromJsonFile(String filePath) throws IOException {

        String jsonContent = Files.readString(Paths.get(filePath));
     System.out.println("json text before mapping: "+jsonContent);
        List<UserCreateDTO> users = Arrays.asList(objectMapper.readValue(jsonContent, UserCreateDTO[].class));

        users.forEach(user -> System.out.println(user.toString()));
        List<User> list = new ArrayList<>();
        users.forEach(user -> list.add(userMapper.map(user)));
        users.forEach(u -> {
            System.out.println("DTO: " + u.getEmail() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getPassword());
        });


        list.forEach(u -> {
            System.out.println("ENTITY: " + u.getEmail() + " " + u.getFirstName() + " " + u.getLastName() + " " + u.getPassword());
        });


        userRepository.saveAll(list);
    }
}