package com.leadpet.www.infrastructure.domain.breed;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Breed: 품종 정보
 */
@Entity
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class Breed {
    @Id
    @Column(name = "breed_id")
    private String breedId;

    @Column(name = "category")
    private String category;

    @Column(name = "breed_name")
    private String breedName;
}
