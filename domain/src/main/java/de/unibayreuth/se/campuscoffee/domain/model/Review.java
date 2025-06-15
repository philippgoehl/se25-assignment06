package de.unibayreuth.se.campuscoffee.domain.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain class that stores the review metadata.
 */
@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class Review implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // required to clone objects (see TestFixtures class).

    private Long id; // id is null when creating a new review

    private LocalDateTime createdAt; // is null when using DTO to create a new review

    private Long posId; // id of the POS being reviewed

    private Long authorId; // id of the user who wrote the review

    @NonNull
    private String review; // the review text

    private Boolean approved; // is null when using DTO to create a new review, true if approved, false if not approved

    // additional fields that are not part of the DTO but are used in the ReviewDtoMapper
    private Pos pos; // the POS being reviewed, not part of the DTO
    private User author; // the user who wrote the review, not part of the DTO
    private Integer approvalCount; // number of approvals for the review, not part of the DTO
}