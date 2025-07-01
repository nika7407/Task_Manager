package hexlet.code.controller;


import hexlet.code.dto.Label.LabelCreateDTO;
import hexlet.code.dto.Label.LabelDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private LabelMapper labelMapper;

    @GetMapping("")
    public ResponseEntity<List<LabelDTO>> getLabels() {

        var unmappedLabels = labelRepository.findAll();
        List<LabelDTO> list = new ArrayList<>();

        var returnList = unmappedLabels.stream().map(labelMapper::map).toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(returnList.size()))
                .body(returnList);
    }

    @GetMapping("/{id}")
    public LabelDTO getLabel(@PathVariable Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("label by id = " + id + " not found"));
        return labelMapper.map(label);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO createLabel(@RequestBody LabelCreateDTO labelCreateDTO){
        var label = labelMapper.map(labelCreateDTO);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    @PutMapping("/{id}")
    public LabelDTO update(@RequestBody LabelCreateDTO labelCreateDTO, @PathVariable Long id){
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("label by id = " + id + " not found"));

        var updated = labelMapper.update(label,labelCreateDTO);
        labelRepository.save(updated);
        return labelMapper.map(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        labelRepository.deleteById(id);
        return;
    }


}
