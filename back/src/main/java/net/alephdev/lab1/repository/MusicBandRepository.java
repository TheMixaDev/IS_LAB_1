package net.alephdev.lab1.repository;

import net.alephdev.lab1.enums.MusicGenre;
import net.alephdev.lab1.models.Album;
import net.alephdev.lab1.models.Coordinates;
import net.alephdev.lab1.models.Label;
import net.alephdev.lab1.models.MusicBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface MusicBandRepository extends JpaRepository<MusicBand, Long> {

    List<MusicBand> findAllByDescriptionContainingIgnoreCase(String description);

    long countAllByEstablishmentDate(LocalDate establishmentDate);

    long countAllByLabelSalesGreaterThan(float sales);

    List<MusicBand> findAllByNameStartingWithIgnoreCase(String prefix);

    List<MusicBand> findAllByGenreOrderByLabel_SalesDesc(MusicGenre genre);

    long countAllByCoordinates(Coordinates coordinates);

    long countAllByBestAlbum(Album album);

    long countAllByLabel(Label label);
}