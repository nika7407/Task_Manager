package hexlet.code.util;

import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Util {

    private final UserRepository userRepository;

    @Autowired
    public Util(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String changeID(String originalTasks) {
        Long id = userRepository.findAll().getFirst().getId();
        return originalTasks.replaceAll("TOCHANGE", String.valueOf(id));
    }
}

