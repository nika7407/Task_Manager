package hexlet.code.controller;


import hexlet.code.dto.label.LabelCreateDTO;
import hexlet.code.dto.label.LabelDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@AllArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @GetMapping("")
    public ResponseEntity<List<LabelDTO>> getLabels() {
        var returnList = labelService.getLabels();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(returnList.size()))
                .body(returnList);
    }

    @GetMapping("/{id}")
    public LabelDTO getLabel(@PathVariable Long id) {
        return labelService.getLabel(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO createLabel(@Valid  @RequestBody LabelCreateDTO labelCreateDTO){
        return labelService.createLabel(labelCreateDTO);
    }

    @PutMapping("/{id}")
    public LabelDTO update(@Valid @RequestBody LabelCreateDTO labelCreateDTO, @PathVariable Long id){
        return labelService.update(labelCreateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        labelService.delete(id);
    }


}
