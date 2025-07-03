package hexlet.code.repository;


import hexlet.code.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.labels WHERE t.id = :id")
    Optional<Task> findWithLabelsById(@Param("id") Long id);
}