package de.unibayreuth.se.campuscoffee.api.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for user metadata.
 *
 */
@Data
@Builder(toBuilder = true)
public class UserDto {
        private Long id; // id is null when creating a new user
        private LocalDateTime createdAt; // is null when using DTO to create a new user
        private LocalDateTime updatedAt; // is set when creating or updating a user
        @NotBlank(message = "Login name cannot be empty.")
        @Size(max = 255, message = "Name can be at most 255 characters long.")
        private final String loginName;
        @Email
        private final String emailAddress;
        @NotBlank(message = "First name cannot be empty.")
        @Size(min = 1, max = 255, message = "Length of first name needs to be in range [1,255].")
        private final String firstName;
        @Size(min = 1, max = 255, message = "Length of last name needs to be in range [1,255].")
        private final String lastName;
}
