package net.alephdev.lab1.service;

import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.models.Album;
import net.alephdev.lab1.repository.AlbumRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    @Transactional
    public Album createAlbum(Album album) {
        return albumRepository.save(album);
    }

    public Album getAlbumById(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));
    }
    public List<Album> getAllAlbums() {
        return albumRepository.findAll();
    }

    @Transactional
    public Album updateAlbum(Long id, Album updatedAlbum) {
        Album existingAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Album not found"));

        existingAlbum.setName(updatedAlbum.getName());
        existingAlbum.setLength(updatedAlbum.getLength());
        existingAlbum.setSales(updatedAlbum.getSales());

        return albumRepository.save(existingAlbum);
    }

    @Transactional
    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }
}