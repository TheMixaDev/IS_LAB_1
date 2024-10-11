package net.alephdev.lab1.service;

import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.models.Label;
import net.alephdev.lab1.repository.LabelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelService {

    private final LabelRepository labelRepository;

    @Transactional
    public Label createLabel(Label label) {
        return labelRepository.save(label);
    }

    public Label getLabelById(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Label not found"));
    }

    @Transactional
    public Label updateLabel(Long id, Label updatedLabel) {
        Label existingLabel = labelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Label not found"));

        existingLabel.setSales(updatedLabel.getSales());

        return labelRepository.save(existingLabel);
    }

    @Transactional
    public void deleteLabel(Long id) {
        labelRepository.deleteById(id);
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }
}