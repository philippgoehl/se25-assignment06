package de.unibayreuth.se.campuscoffee.data.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Database entity for a review.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_sequence_generator")
    @SequenceGenerator(name = "review_sequence_generator", sequenceName = "review_seq", allocationSize = 1)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "pos_id")
    private PosEntity pos;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private UserEntity author;

    private String review;

    @Column(name = "approval_count")
    private Integer approvalCount;
}
