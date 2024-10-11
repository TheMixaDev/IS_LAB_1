package net.alephdev.lab1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.annotation.AuthorizedRequired;
import net.alephdev.lab1.models.Coordinates;
import net.alephdev.lab1.service.CoordinatesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coordinates")
@RequiredArgsConstructor
@AuthorizedRequired
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @PostMapping
    public ResponseEntity<Coordinates> createCoordinates(@Valid @RequestBody Coordinates coordinates) {
        return new ResponseEntity<>(coordinatesService.createCoordinates(coordinates), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coordinates> getCoordinatesById(@PathVariable Long id) {
        return new ResponseEntity<>(coordinatesService.getCoordinatesById(id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Coordinates>> getAllCoordinates() {
        return new ResponseEntity<>(coordinatesService.getAllCoordinates(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coordinates> updateCoordinates(@PathVariable Long id, @Valid @RequestBody Coordinates updatedCoordinates) {
        return new ResponseEntity<>(coordinatesService.updateCoordinates(id, updatedCoordinates), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinates(@PathVariable Long id) {
        coordinatesService.deleteCoordinates(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}