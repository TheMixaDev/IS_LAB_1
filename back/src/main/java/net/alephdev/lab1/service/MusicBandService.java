package net.alephdev.lab1.service;

import lombok.RequiredArgsConstructor;
import net.alephdev.lab1.WebSocketHandler;
import net.alephdev.lab1.dto.MusicBandRequestDto;
import net.alephdev.lab1.enums.EventType;
import net.alephdev.lab1.enums.MusicGenre;
import net.alephdev.lab1.models.*;
import net.alephdev.lab1.repository.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MusicBandService {

    private final MusicBandRepository musicBandRepository;
    private final CoordinatesRepository coordinatesRepository;
    private final AlbumRepository albumRepository;
    private final LabelRepository labelRepository;
    private final EventRepository eventRepository;
    private final WebSocketHandler webSocketHandler;

    private void createEvent(MusicBand musicBand, User user, EventType eventType, String description) {
        Event event = new Event();
        event.setMusicBand(musicBand);
        event.setUser(user);
        event.setEventType(eventType);
        event.setDescription(description);
        eventRepository.save(event);
    }

    @Transactional
    public MusicBand createMusicBand(MusicBandRequestDto musicBandRequestDto, User user) {
        Coordinates coordinates = coordinatesRepository.findById(musicBandRequestDto.getCoordinatesId())
                .orElseThrow(() -> new IllegalArgumentException("Coordinates not found"));

        Album bestAlbum = musicBandRequestDto.getBestAlbumId() != null
                ? albumRepository.findById(musicBandRequestDto.getBestAlbumId())
                .orElseThrow(() -> new IllegalArgumentException("Album not found"))
                : null;

        Label label = musicBandRequestDto.getLabelId() != null
                ? labelRepository.findById(musicBandRequestDto.getLabelId())
                .orElseThrow(() -> new IllegalArgumentException("Label not found"))
                : null;

        MusicBand musicBand = new MusicBand();
        musicBand.setName(musicBandRequestDto.getName());
        musicBand.setCoordinates(coordinates);
        musicBand.setGenre(musicBandRequestDto.getGenre());
        musicBand.setNumberOfParticipants(musicBandRequestDto.getNumberOfParticipants());
        musicBand.setSinglesCount(musicBandRequestDto.getSinglesCount());
        musicBand.setDescription(musicBandRequestDto.getDescription());
        musicBand.setBestAlbum(bestAlbum);
        musicBand.setAlbumsCount(musicBandRequestDto.getAlbumsCount());
        musicBand.setEstablishmentDate(musicBandRequestDto.getEstablishmentDate());
        musicBand.setLabel(label);
        musicBand.setCreatedBy(user);
        MusicBand savedMusicBand = musicBandRepository.save(musicBand);
        webSocketHandler.notifyClients(EventType.CREATE, savedMusicBand.getId());

        createEvent(savedMusicBand, user, EventType.CREATE, "Группа создана");
        return savedMusicBand;
    }

    public List<MusicBand> getAllMusicBands() {
        return musicBandRepository.findAll();
    }

    public MusicBand getMusicBandById(Long id) {
        return musicBandRepository.findById(id).orElseThrow();
    }
    @Transactional
    public MusicBand updateMusicBand(Long id, MusicBandRequestDto updatedMusicBand, User user) {
        MusicBand existingMusicBand = musicBandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));

        if (!existingMusicBand.getCreatedBy().equals(user) && !user.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Вы не можете редактировать эту группу");
        }

        existingMusicBand.setName(updatedMusicBand.getName());
        existingMusicBand.setNumberOfParticipants(updatedMusicBand.getNumberOfParticipants());
        existingMusicBand.setSinglesCount(updatedMusicBand.getSinglesCount());
        existingMusicBand.setDescription(updatedMusicBand.getDescription());
        existingMusicBand.setAlbumsCount(updatedMusicBand.getAlbumsCount());
        existingMusicBand.setEstablishmentDate(updatedMusicBand.getEstablishmentDate());
        existingMusicBand.setGenre(updatedMusicBand.getGenre());

        if (updatedMusicBand.getCoordinatesId() != null) {
            Coordinates coordinates = coordinatesRepository.findById(updatedMusicBand.getCoordinatesId())
                    .orElseThrow(() -> new IllegalArgumentException("Coordinates not found"));
            existingMusicBand.setCoordinates(coordinates);
        }

        if (updatedMusicBand.getBestAlbumId() != null) {
            Album album = albumRepository.findById(updatedMusicBand.getBestAlbumId())
                    .orElseThrow(() -> new IllegalArgumentException("Album not found"));
            existingMusicBand.setBestAlbum(album);
        }

        if (updatedMusicBand.getLabelId() != null) {
            Label label = labelRepository.findById(updatedMusicBand.getLabelId())
                    .orElseThrow(() -> new IllegalArgumentException("Label not found"));
            existingMusicBand.setLabel(label);
        }

        MusicBand resultMusicBand = musicBandRepository.save(existingMusicBand);
        webSocketHandler.notifyClients(EventType.UPDATE, resultMusicBand.getId());
        createEvent(resultMusicBand, user, EventType.UPDATE, "Группа обновлена");
        return resultMusicBand;
    }
    @Transactional
    public void deleteMusicBand(Long id, User user) {
        MusicBand musicBand = musicBandRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));

        if (!musicBand.getCreatedBy().equals(user) && !user.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Вы не можете удалить эту группу");
        }

        String deletedMusicBandName = musicBand.getName();

        eventRepository.findAllByMusicBand(musicBand).forEach(event -> {
            event.setMusicBand(null);
            event.setDescription(event.getDescription() + " (группа \"" + deletedMusicBandName + "\" была удалена)");
            eventRepository.save(event);
        });

        createEvent(null, user, EventType.DELETE, "Группа \"" + deletedMusicBandName + "\" удалена");

        musicBandRepository.delete(musicBand);
        webSocketHandler.notifyClients(EventType.DELETE, musicBand.getId());

        deleteUnrelatedCoordinates(musicBand.getCoordinates());
        deleteUnrelatedAlbum(musicBand.getBestAlbum());
        deleteUnrelatedLabel(musicBand.getLabel());
    }

    private void deleteUnrelatedCoordinates(Coordinates coordinates) {
        if (coordinates != null && musicBandRepository.countAllByCoordinates(coordinates) == 0) {
            coordinatesRepository.delete(coordinates);
        }
    }

    private void deleteUnrelatedAlbum(Album album) {
        if (album != null && musicBandRepository.countAllByBestAlbum(album) == 0) {
            albumRepository.delete(album);
        }
    }

    private void deleteUnrelatedLabel(Label label) {
        if (label != null && musicBandRepository.countAllByLabel(label) == 0) {
            labelRepository.delete(label);
        }
    }


    public long countByEstablishmentDate(LocalDate establishmentDate) {
        return musicBandRepository.countAllByEstablishmentDate(establishmentDate);
    }

    public long countByLabelSalesGreaterThan(float sales) {
        return musicBandRepository.countAllByLabelSalesGreaterThan(sales);
    }

    public List<MusicBand> findByNameStartingWith(String prefix) {
        return musicBandRepository.findAllByNameStartingWithIgnoreCase(prefix);
    }

    @Transactional
    public void addSingleToMusicBand(Long id, User user) {
        MusicBand musicBand = musicBandRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Группа не найдена"));
        if (musicBand.getSinglesCount() == null) {
            musicBand.setSinglesCount(1);
        } else {
            musicBand.setSinglesCount(musicBand.getSinglesCount() + 1);
        }
        webSocketHandler.notifyClients(EventType.UPDATE, musicBand.getId());
        createEvent(musicBand, user, EventType.UPDATE, "Добавлен сингл");
        musicBandRepository.save(musicBand);
    }

    @Transactional
    public void rewardBestMusicBand(MusicGenre genre) {
        List<MusicBand> bestMusicBands = musicBandRepository.findAllByGenreOrderByLabel_SalesDesc(genre);
        if (!bestMusicBands.isEmpty()) {
            MusicBand bestMusicBand = bestMusicBands.get(0);
            String description = bestMusicBand.getDescription();
            if (description == null) {
                description = "";
            }
            bestMusicBand.setDescription(description + "\n Награждена как лучшая в жанре " + genre.getName() + "!");
            musicBandRepository.save(bestMusicBand);
            webSocketHandler.notifyClients(EventType.REWARD, bestMusicBand.getId());
            createEvent(bestMusicBand, bestMusicBand.getCreatedBy(), EventType.REWARD,
                    "Группа награждена как лучшая в жанре " + genre.getName());
        }
    }
}