package net.alephdev.lab1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.annotation.AuthorizedRequired;
import net.alephdev.lab1.models.Album;
import net.alephdev.lab1.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
@AuthorizedRequired
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<Album> createAlbum(@Valid @RequestBody Album album) {
        return new ResponseEntity<>(albumService.createAlbum(album), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable Long id) {
        return new ResponseEntity<>(albumService.getAlbumById(id), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        return new ResponseEntity<>(albumService.getAllAlbums(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable Long id, @Valid @RequestBody Album updatedAlbum) {
        return new ResponseEntity<>(albumService.updateAlbum(id, updatedAlbum), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}