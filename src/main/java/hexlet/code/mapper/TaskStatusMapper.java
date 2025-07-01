package hexlet.code.mapper;

import hexlet.code.model.TaskStatus;
import hexlet.code.dto.TaskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatus.TaskStatusDTO;
import hexlet.code.dto.TaskStatus.TaskStatusUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        //uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskStatusMapper {
    TaskStatus map(TaskStatusDTO dto);

    TaskStatusDTO map(TaskStatus object);

    TaskStatus map(TaskStatusCreateDTO createDTO);

    void update(TaskStatusUpdateDTO dto, @MappingTarget TaskStatus toUpdate);
}
