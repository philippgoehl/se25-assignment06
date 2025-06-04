package de.unibayreuth.se.campuscoffee.data.persistence;

import de.unibayreuth.se.campuscoffee.domain.model.CampusType;
import de.unibayreuth.se.campuscoffee.domain.model.PosType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Database entity for a point-of-sale (POS).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pos")
public class PosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pos_sequence_generator")
    @SequenceGenerator(name = "pos_sequence_generator", sequenceName = "pos_seq", allocationSize = 1)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(unique = true)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private PosType type;

    @Enumerated(EnumType.STRING)
    private CampusType campus;

    @Embedded
    private AddressEntity address;
}
