package hexlet.code.mapper;


import hexlet.code.Model.Label;
import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.dto.Label.LabelDTO;
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
public abstract class LabelMapper {
    public abstract Label map(LabelDTO labelDTO);

    public abstract LabelDTO map(Label label);

    public abstract Label map(LabelCreateDTO labelCreateDTO);

   public abstract Label update(@MappingTarget Label label, LabelCreateDTO labelCreateDTO);
}
