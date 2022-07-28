package com.leadpet.www.infrastructure.domain.breed;

import com.leadpet.www.infrastructure.domain.BaseTime;

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
public class Breed extends BaseTime {
    @Id
    @Column(name = "breed_id")
    private String breedId;

    @Column(name = "category")
    private String category;

    @Column(name = "breed_name")
    private String breedName;
}
