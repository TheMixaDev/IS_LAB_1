package net.alephdev.lab1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.annotation.AuthorizedRequired;
import net.alephdev.lab1.models.Label;
import net.alephdev.lab1.service.LabelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
@AuthorizedRequired
public class LabelController {

    private final LabelService labelService;

    @GetMapping
    public ResponseEntity<List<Label>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        return ResponseEntity.ok(labels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabelById(@PathVariable Long id) {
        Label label = labelService.getLabelById(id);
        return ResponseEntity.ok(label);
    }

    @PostMapping
    public ResponseEntity<Label> createLabel(@Valid @RequestBody Label label) {
        Label createdLabel = labelService.createLabel(label);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Label> updateLabel(@PathVariable Long id, @Valid @RequestBody Label label) {
        Label updatedLabel = labelService.updateLabel(id, label);
        return ResponseEntity.ok(updatedLabel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}