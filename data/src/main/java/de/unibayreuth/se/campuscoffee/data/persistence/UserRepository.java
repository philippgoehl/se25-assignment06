package de.unibayreuth.se.campuscoffee.data.persistence;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * Repository for persisting user entities.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginName(String loginName);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE user_seq RESTART WITH 1", nativeQuery = true)
    void resetSequence();
}
