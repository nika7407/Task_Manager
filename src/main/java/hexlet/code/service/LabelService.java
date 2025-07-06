package hexlet.code.service;

import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public List<LabelDTO> getLabels() {
        var unmappedLabels = labelRepository.findAll();
        return unmappedLabels.stream().map(labelMapper::map).toList();
    }

    public LabelDTO getLabel(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("label by id = " + id + " not found"));
        return labelMapper.map(label);
    }

    public LabelDTO createLabel(LabelCreateDTO labelCreateDTO) {
        var label = labelMapper.map(labelCreateDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO update(LabelCreateDTO labelCreateDTO, Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("label by id = " + id + " not found"));

        var updated = labelMapper.update(label, labelCreateDTO);
        labelRepository.save(updated);
        return labelMapper.map(updated);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
