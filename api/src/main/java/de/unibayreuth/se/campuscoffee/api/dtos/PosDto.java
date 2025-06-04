package de.unibayreuth.se.campuscoffee.api.dtos;

import de.unibayreuth.se.campuscoffee.domain.model.CampusType;
import de.unibayreuth.se.campuscoffee.domain.model.PosType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for POS metadata.
 *
 */
@Data
@Builder(toBuilder = true)
public class PosDto {
        private Long id; // id is null when creating a new task
        private LocalDateTime createdAt; // is null when using DTO to create a new POS
        private LocalDateTime updatedAt; // is set when creating or updating a POS
        @NotBlank(message = "Name cannot be empty.")
        @Size(max = 255, message = "Name can be at most 255 characters long.")
        private final String name;
        @NotBlank(message = "Description cannot be empty.")
        private final String description;
        @NotNull
        private final PosType type;
        @NotNull
        private final CampusType campus;
        @NotBlank
        private final String street;
        @Size(min = 1, max = 255, message = "Length of house number needs to be in range [1,255].")
        private final String houseNumber;
        @NotNull
        private final Integer postalCode;
        @Size(min = 1, max = 255, message = "Length of city needs to be in range [1,255].")
        private final String city;
}
