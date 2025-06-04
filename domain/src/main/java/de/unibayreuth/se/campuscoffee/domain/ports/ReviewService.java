package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.exceptions.*;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.model.Review;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the review service that the domain layer provides as a port.
 */
public interface ReviewService {
    void clear();
    @NonNull
    List<Review> getAll();
    @NonNull
    Review getById(@NonNull Long id) throws ReviewNotFoundException;
    @NonNull
    List<Review> getApprovedByPos(@NonNull Pos pos) throws PosNotFoundException;
    @NonNull
    Review create(@NonNull Review review) throws PosNotFoundException, UserNotFoundException;
    @NonNull
    Review approve(@NonNull Review review, @NonNull User user) throws ReviewNotFoundException, UserNotFoundException, IllegalArgumentException;
}
