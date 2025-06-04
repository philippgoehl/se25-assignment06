package de.unibayreuth.se.campuscoffee.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Domain class that stores the user metadata.
 */
@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // required to clone objects (see TestFixtures class).

    private Long id; // null when the user has not been created yet
    private LocalDateTime createdAt; // set on user creation
    private LocalDateTime updatedAt; // set on user creation and update
    @NonNull
    private String loginName;
    @NonNull
    private String emailAddress;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
}
