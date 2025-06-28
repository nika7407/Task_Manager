package hexlet.code.mapper;

import hexlet.code.Model.User;
import hexlet.code.dto.User.UserCreateDTO;
import hexlet.code.dto.User.UserDTO;
import hexlet.code.dto.User.UserUpdateDTO;
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
public interface UserMapper {
      User map(UserCreateDTO dto);
      User map(UserDTO dto);
      UserDTO map(User user);
      void update(UserUpdateDTO dto, @MappingTarget User toUpdate);
}

