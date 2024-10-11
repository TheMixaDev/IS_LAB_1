package net.alephdev.lab1.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.alephdev.lab1.enums.MusicGenre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "music_bands")
public class MusicBand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id")
    @NotNull
    private Coordinates coordinates;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private MusicGenre genre;

    @Column(nullable = false)
    @Min(1)
    private long numberOfParticipants;

    @Min(0)
    private Integer singlesCount;

    @Column(nullable = false)
    @NotBlank
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "best_album_id")
    private Album bestAlbum;

    @Column(nullable = false)
    @Min(0)
    private int albumsCount;

    @Column(nullable = false)
    @NotNull
    private LocalDate establishmentDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "label_id")
    private Label label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @PrePersist
    private void prePersist() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    private void preUpdate() {
        if (creationDate == null) {
            creationDate = LocalDateTime.now();
        }
    }
}