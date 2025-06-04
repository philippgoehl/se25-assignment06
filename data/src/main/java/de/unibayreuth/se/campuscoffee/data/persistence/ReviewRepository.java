package de.unibayreuth.se.campuscoffee.data.persistence;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for persisting review entities.
 */
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findAllByPos(PosEntity pos);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE review_seq RESTART WITH 1", nativeQuery = true)
    void resetSequence();
}
