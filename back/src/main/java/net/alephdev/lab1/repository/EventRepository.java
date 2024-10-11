package net.alephdev.lab1.repository;

import net.alephdev.lab1.models.Event;
import net.alephdev.lab1.models.MusicBand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByMusicBand(MusicBand musicBand);
}