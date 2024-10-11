package net.alephdev.lab1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.annotation.AuthorizedRequired;
import net.alephdev.lab1.annotation.CurrentUser;
import net.alephdev.lab1.dto.MusicBandDto;
import net.alephdev.lab1.dto.MusicBandRequestDto;
import net.alephdev.lab1.dto.UserAuthorizedDto;
import net.alephdev.lab1.enums.MusicGenre;
import net.alephdev.lab1.models.MusicBand;
import net.alephdev.lab1.models.User;
import net.alephdev.lab1.service.MusicBandService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/music-bands")
@RequiredArgsConstructor
@AuthorizedRequired
public class MusicBandController {

    private final MusicBandService musicBandService;

    @PostMapping
    public ResponseEntity<MusicBand> createMusicBand(@Valid @RequestBody MusicBandRequestDto musicBandRequestDto, @CurrentUser User user) {
        return new ResponseEntity<>(musicBandService.createMusicBand(musicBandRequestDto, user), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MusicBand> updateMusicBand(@PathVariable Long id, @Valid @RequestBody MusicBandRequestDto musicBandRequestDto, @CurrentUser User user) {
        try {
            return new ResponseEntity<>(musicBandService.updateMusicBand(id, musicBandRequestDto, user), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public ResponseEntity<List<MusicBandDto>> getAllMusicBands() {
        List<MusicBand> musicBands = musicBandService.getAllMusicBands();
        List<MusicBandDto> musicBandDtos = musicBands.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(musicBandDtos, HttpStatus.OK);
    }

    private MusicBandDto convertToDto(MusicBand musicBand) {
        MusicBandDto dto = new MusicBandDto();
        dto.setId(musicBand.getId());
        dto.setName(musicBand.getName());
        dto.setCoordinates(musicBand.getCoordinates());
        dto.setCreationDate(musicBand.getCreationDate());
        dto.setGenre(musicBand.getGenre());
        dto.setNumberOfParticipants(musicBand.getNumberOfParticipants());
        dto.setSinglesCount(musicBand.getSinglesCount());
        dto.setDescription(musicBand.getDescription());
        dto.setBestAlbum(musicBand.getBestAlbum());
        dto.setAlbumsCount(musicBand.getAlbumsCount());
        if (musicBand.getEstablishmentDate() != null) {
            dto.setEstablishmentDate(musicBand.getEstablishmentDate());
        }
        dto.setEstablishmentDate(musicBand.getEstablishmentDate());
        dto.setLabel(musicBand.getLabel());
        if (musicBand.getCreatedBy() != null) {
            dto.setCreatedBy(convertToUserDto(musicBand.getCreatedBy()));
        }
        return dto;
    }

    private UserAuthorizedDto convertToUserDto(User user) {
        UserAuthorizedDto dto = new UserAuthorizedDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicBandDto> getMusicBandById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(convertToDto(musicBandService.getMusicBandById(id)), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMusicBand(@PathVariable Long id, @CurrentUser User user) {
        try {
            musicBandService.deleteMusicBand(id, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/count/establishment-date")
    public ResponseEntity<Long> countByEstablishmentDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate establishmentDate) {
        long count = musicBandService.countByEstablishmentDate(establishmentDate);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/label-sales")
    public ResponseEntity<Long> countByLabelSalesGreaterThan(@RequestParam("sales") float sales) {
        long count = musicBandService.countByLabelSalesGreaterThan(sales);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/name/{prefix}")
    public ResponseEntity<List<MusicBandDto>> findByNameStartingWith(@PathVariable("prefix") String prefix) {
        List<MusicBand> musicBands = musicBandService.findByNameStartingWith(prefix);
        List<MusicBandDto> musicBandDtos = musicBands.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(musicBandDtos);
    }

    @PostMapping("/{id}/add-single")
    public ResponseEntity<Void> addSingleToMusicBand(@PathVariable("id") Long id, @CurrentUser User user) {
        try {
            musicBandService.addSingleToMusicBand(id, user);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reward-best/{genre}")
    public ResponseEntity<Void> rewardBestMusicBand(@PathVariable("genre") MusicGenre genre) {
        musicBandService.rewardBestMusicBand(genre);
        return ResponseEntity.ok().build();
    }
}