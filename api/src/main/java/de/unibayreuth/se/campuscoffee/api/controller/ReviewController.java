package de.unibayreuth.se.campuscoffee.api.controller;

import de.unibayreuth.se.campuscoffee.api.dtos.ReviewDto;
import de.unibayreuth.se.campuscoffee.api.dtos.UserDto;
import de.unibayreuth.se.campuscoffee.api.mapper.ReviewDtoMapper;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.ReviewNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.Review;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import de.unibayreuth.se.campuscoffee.domain.ports.ReviewService;
import de.unibayreuth.se.campuscoffee.domain.ports.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(
                title = "CampusCoffee",
                version = "0.0.1"
        )
)
@Tag(name = "Reviews")
@Controller
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final PosService posService;
    private final ReviewDtoMapper reviewDtoMapper;


    @Operation(
        summary = "Get all reviews.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(type = "array", implementation = ReviewDto.class)
                        ),
                        description = "All reviews as a JSON array."
                )
        }
    )
    @GetMapping("")
    public ResponseEntity<List<ReviewDto>> getAll() {
        return ResponseEntity.ok(
                reviewService.getAll().stream()
                        .map(reviewDtoMapper::fromDomain)
                        .toList()
        );
    }


    @Operation(
        summary = "Get review by ID.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ReviewDto.class)
                        ),
                        description = "The review with the provided ID as a JSON object."
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "No review with the provided ID could be found."
                )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(
                    reviewDtoMapper.fromDomain(reviewService.getById(id))
            );
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No review found with ID: " + id, e);
        }
    }


    @Operation(
        summary = "List approved reviews.",
        description = "Returns all reviews that have been approved for the specified POS ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReviewDto.class)
                ),
                description = "A JSON array of approved ReviewDto objects."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No reviews found for the given pos_id."
            )
        }
    )
    @GetMapping(params = "pos_id")
    public ResponseEntity<List<ReviewDto>> getApprovedByPosId(@RequestParam("pos_id") Long posId) {
        try {
            List<Review> reviews = reviewService.getApprovedByPos(posService.getById(posId));
            if (reviews.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No reviews found for POS ID: " + posId);
            }
            return ResponseEntity.ok(
                    reviews.stream()
                            .map(reviewDtoMapper::fromDomain)
                            .toList()
            );
        } catch (PosNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No POS found with ID: " + posId, e);
        }
    }
    

    @Operation(
            summary = "Creates a new review.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ReviewDto.class)
                            ),
                            description = "The new review as a JSON object."
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "ID is not empty."
                    )
            }
    )
    @PostMapping("")
    public ResponseEntity<ReviewDto> create(@RequestBody @Valid ReviewDto reviewDto) {
        if (reviewDto.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be null when creating a new review.");
        }
        try {
            Review review = reviewDtoMapper.toDomain(reviewDto);
            Review createdReview = reviewService.create(review);
            return ResponseEntity.ok(reviewDtoMapper.fromDomain(createdReview));
        } catch (UserNotFoundException | PosNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user or POS data.", e);
        }

    }
    

    @Operation(
        summary = "Approves a review.",
        description = "Marks the review identified by `{id}` as approved.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReviewDto.class)
                ),
                description = "The approved review as a JSON object."
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid user data or request."
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No review or user could be found with the provided ID."
            )
        }
    )
    @PutMapping("/{id}/approve")
    public ResponseEntity<ReviewDto> approve(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        try {
            Review review = reviewService.getById(id);
            User user = userService.getById(userDto.getId());
            Review approvedReview = reviewService.approve(review, user);
            return ResponseEntity.ok(reviewDtoMapper.fromDomain(approvedReview));
        } catch (ReviewNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No review found with ID: " + id, e);
        } catch (UserNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user data.", e);
        }
    }
}
