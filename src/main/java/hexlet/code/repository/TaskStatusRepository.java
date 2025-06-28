package hexlet.code.repository;

import hexlet.code.Model.TaskStatus;
import hexlet.code.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {
    Optional<TaskStatus> findBySlug(String slug);
}
