package com.leadpet.www.infrastructure.db.breed;

import com.leadpet.www.infrastructure.domain.breed.Breed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * BreedRepository
 */
@Repository
public interface BreedRepository extends JpaRepository<Breed, String> {
}
