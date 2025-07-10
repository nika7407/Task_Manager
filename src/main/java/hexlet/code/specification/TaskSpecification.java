// TaskSpecification.java
package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    public Specification<Task> build(TaskParamsDTO params) {
        return withTitle(params.getTitleCont())
                .and(withAssigneeId(params.getAssigneeId()))
                .and(withStatusSlug(params.getStatus()))
                .and(withLabelId(params.getLabelId()));
    }

    private Specification<Task> withTitle(String title) {
        return (root, query, cb) -> title == null
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("name")), "%" + title.toLowerCase() + "%");
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, cb) -> assigneeId == null
                ? cb.conjunction()
                : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withStatusSlug(String slug) {
        return (root, query, cb) -> slug == null
                ? cb.conjunction()
                : cb.equal(root.get("taskStatus").get("slug"), slug);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, cb) -> {
            if (labelId == null) return cb.conjunction();
            Join<Object, Object> labels = root.join("labels");
            return cb.equal(labels.get("id"), labelId);
        };
    }
}
