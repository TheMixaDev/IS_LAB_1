package net.alephdev.lab1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.alephdev.lab1.enums.MusicGenre;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MusicBandRequestDto {

    @NotBlank
    private String name;

    @NotNull
    private Long coordinatesId;

    @NotNull
    private MusicGenre genre;

    @Min(1)
    private long numberOfParticipants;

    @Min(0)
    private Integer singlesCount;

    @NotBlank
    private String description;

    private Long bestAlbumId;

    @Min(0)
    private int albumsCount;

    @NotNull
    private LocalDate establishmentDate;

    private Long labelId;
}