package de.unibayreuth.se.campuscoffee.domain.model;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Domain class that stores the review metadata.
 */
@Data
@Builder(toBuilder = true)
//@RequiredArgsConstructor
//@AllArgsConstructor
public class Review implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // required to clone objects (see TestFixtures class).

    // TODO: define Review domain class (uncomment Lombok annotations after defining the class)
}
