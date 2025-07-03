package hexlet.code.controller;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user by id = " + id + "not found"));
        return userMapper.map(user);
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getUsers() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(userMapper::map)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(result);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO createDTO) {
        User userToAdd = userMapper.map(createDTO);
        userRepository.save(userToAdd);
      return userMapper.map(userToAdd);

    }

    @PutMapping("/{id}")
    @PreAuthorize("@userUtils.isAuthor(#id)")
    public UserDTO update(@RequestBody UserUpdateDTO updateDTO, @PathVariable Long id) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user by id = " + id + "not found"));

        userMapper.update(updateDTO, user);
        userRepository.save(user);

        return userMapper.map(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@userUtils.isAuthor(#id)")
    public void delete(@PathVariable Long id){
        userRepository.deleteById(id);
        return;
    }
}
