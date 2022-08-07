package com.leadpet.www.infrastructure.domain.breed;

import com.leadpet.www.infrastructure.domain.BaseTime;
import com.leadpet.www.infrastructure.domain.pet.AnimalType;

import javax.persistence.*;

/**
 * Breed: 품종 정보
 */
@Entity
@lombok.Getter
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class Breed extends BaseTime {
    @Id
    @Column(name = "breed_id")
    private String breedId;

    @Column(name = "category")
    private String category;

    @Column(name = "breed_name")
    private String breedName;

    @Column(name = "animal_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AnimalType animalType;
}
