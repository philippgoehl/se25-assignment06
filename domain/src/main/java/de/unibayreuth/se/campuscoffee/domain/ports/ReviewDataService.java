package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.ReviewNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.model.Review;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the review data service that the domain layer provides as a port.
 */
public interface ReviewDataService {
    void clear();
    @NonNull
    List<Review> getAll();
    @NonNull
    Review getById(@NonNull Long id) throws ReviewNotFoundException;
    @NonNull
    List<Review> getByPos(@NonNull Pos pos) throws PosNotFoundException;
    @NonNull
    Review create(@NonNull Review review);
    @NonNull
    Review approve(@NonNull Review review) throws ReviewNotFoundException;
}
