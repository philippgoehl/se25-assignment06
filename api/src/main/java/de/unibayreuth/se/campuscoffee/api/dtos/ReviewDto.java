package de.unibayreuth.se.campuscoffee.api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for review metadata.
 *
 */
@Data
@Builder(toBuilder = true)
public class ReviewDto {
        private Long id; // id is null when creating a new review

        private LocalDateTime createdAt; // is null when using DTO to create a new review

        private Long posId; 

        private Long authorId;

        @NotBlank(message = "Review cannot be empty.")
        private final String review;

        private Boolean approved; // is null when using DTO to create a new review, true if approved, false if not approved
}
