package de.unibayreuth.se.campuscoffee.domain.impl;

import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.ReviewNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.model.Review;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import de.unibayreuth.se.campuscoffee.domain.ports.ReviewService;
import de.unibayreuth.se.campuscoffee.domain.ports.ReviewDataService;
import de.unibayreuth.se.campuscoffee.domain.ports.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDataService reviewDataService;
    private final PosService posService;
    private final UserService userService;

    @Override
    public void clear() {
        reviewDataService.clear();
    }

    @Override
    @NonNull
    public List<Review> getAll() {
        return reviewDataService.getAll();
    }

    @Override
    @NonNull
    public Review getById(@NonNull Long id) throws ReviewNotFoundException {
        return reviewDataService.getById(id);
    }

    @Override
    @NonNull
    public List<Review> getApprovedByPos(@NonNull Pos pos) throws PosNotFoundException {
        Pos validated = posService.getById(pos.getId());
        return reviewDataService.getByPos(validated).stream()
                                .filter(r -> Boolean.TRUE.equals(r.getApproved()))
                                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public Review create(@NonNull Review review)
            throws PosNotFoundException, UserNotFoundException {
        // validate
        posService.getById(review.getPosId());
        userService.getById(review.getAuthorId());
        // init
        review.setCreatedAt(LocalDateTime.now());
        review.setApproved(false);
        review.setApprovalCount(0);
        // create
        return reviewDataService.create(review);
    }

    @Override
    @NonNull
    public Review approve(@NonNull Review review, @NonNull User user)
            throws ReviewNotFoundException, UserNotFoundException, IllegalArgumentException {
        Review existing = getById(review.getId());
        User approver = userService.getById(user.getId());

        // users cannot approve their own reviews
        if (Objects.equals(existing.getAuthorId(), approver.getId())) {
            throw new IllegalArgumentException("Users cannot approve their own review.");
        }

        // approval count increased by one  
        int current = existing.getApprovalCount() == null ? 0 : existing.getApprovalCount();
        existing.setApprovalCount(current + 1);

        // approved once we have ≥2 approvals
        if (existing.getApprovalCount() >= 2) {
            existing.setApproved(true);
        }

        // set approver and approval date
        return reviewDataService.approve(existing);
    }
}
