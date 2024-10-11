package net.alephdev.lab1.models;

import jakarta.persistence.*;
import lombok.*;
import net.alephdev.lab1.enums.EventType;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_band_id")
    private MusicBand musicBand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    private String description;

    @PrePersist
    private void prePersist() {
        timestamp = LocalDateTime.now();
    }
}