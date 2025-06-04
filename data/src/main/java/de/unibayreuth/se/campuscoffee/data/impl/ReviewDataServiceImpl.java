package de.unibayreuth.se.campuscoffee.data.impl;

//import de.unibayreuth.se.campuscoffee.data.mapper.ReviewEntityMapper;
//import de.unibayreuth.se.campuscoffee.data.persistence.*;
//import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
//import de.unibayreuth.se.campuscoffee.domain.exceptions.ReviewNotFoundException;
//import de.unibayreuth.se.campuscoffee.domain.model.Pos;
//import de.unibayreuth.se.campuscoffee.domain.model.Review;
//import de.unibayreuth.se.campuscoffee.domain.ports.ReviewDataService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.List;

// TODO: uncomment the data service implementation after implementing the Review domain class and re-added the ReviewEntityMapper

/**
 * Implementation of the POS data service that the domain layer provides as a port.
 */
//@Service
//@RequiredArgsConstructor
//class ReviewDataServiceImpl implements ReviewDataService {
//    private final ReviewRepository reviewRepository;
//    private final ReviewEntityMapper reviewEntityMapper;
//    private final PosRepository posRepository;
//
//    @Override
//    public void clear() {
//        reviewRepository.deleteAllInBatch();
//        reviewRepository.flush();
//        reviewRepository.resetSequence();
//    }
//
//    @Override
//    @NonNull
//    public List<Review> getAll() {
//        return reviewRepository.findAll().stream()
//                .map(reviewEntityMapper::fromEntity)
//                .toList();
//    }
//
//    @Override
//    @NonNull
//    public Review getById(@NonNull Long id) throws ReviewNotFoundException {
//        return reviewRepository.findById(id)
//                .map(reviewEntityMapper::fromEntity)
//                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + id + " does not exist."));
//    }
//
//    @Override
//    @NonNull
//    public List<Review> getByPos(@NonNull Pos pos) throws PosNotFoundException {
//        PosEntity posEntity = posRepository.findById(pos.getId())
//                .orElseThrow(() -> new PosNotFoundException("POS with ID " + pos.getId() + " does not exist."));
//
//        return reviewRepository.findAllByPos(posEntity)
//                .stream()
//                .map(reviewEntityMapper::fromEntity)
//                .toList();
//    }
//
//    @Override
//    @NonNull
//    public Review create(@NonNull Review review) {
//        review.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
//        review.setApprovalCount(0);
//        return reviewEntityMapper.fromEntity(reviewRepository.saveAndFlush(reviewEntityMapper.toEntity(review)));
//    }
//
//    @Override
//    @NonNull
//    public Review approve(@NonNull Review review) throws ReviewNotFoundException {
//        ReviewEntity reviewEntity = reviewRepository.findById(review.getId())
//                .orElseThrow(() -> new ReviewNotFoundException("Review with ID " + review.getId() + " does not exist."));
//        reviewEntity.setApprovalCount(reviewEntity.getApprovalCount() + 1);
//        return reviewEntityMapper.fromEntity(reviewRepository.saveAndFlush(reviewEntity));
//    }
//}
