package com.leadpet.www.infrastructure.db.breed

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.domain.breed.Breed
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.util.stream.IntStream

/**
 * BreedRepositorySpec
 */
@DataJpaTest
@Import(TestConfig)
@Transactional
class BreedRepositorySpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    BreedRepository breedRepository

    def "품종 정보를 등록"() {
        given:
        def breed = Breed.builder()
                .breedId(breedId)
                .category('가')
                .breedName('골든 리드리버')
                .animalType(AnimalType.DOG)
                .build()

        when: '품종 정보 저장'
        breedRepository.save(breed)
        em.flush()
        em.clear()

        then:
        breedRepository.findById(breedId).orElseThrow()

        where:
        breedId << ['breedId']
    }

    def "품종 리스트를 취득"() {
        given:
        IntStream.range(0, 10).forEach(idx -> {
            boolean isEven = idx % 2 == 0
            breedRepository.save(
                    Breed.builder()
                            .breedId('breedId' + idx)
                            .category(isEven ? '가' : '나')
                            .breedName((isEven ? '골든 리드리버' : '나로 시작하는 견종') + idx)
                            .animalType(AnimalType.DOG)
                            .build()
            )
        })

        when:
        def allBreed = breedRepository.findAll()

        then:
        allBreed != null
        allBreed.size() == 10
    }

    @Unroll
    def "품종 전체 카운트 취득"() {
        given:
        IntStream.range(0, count).forEach(idx -> {
            boolean isEven = idx % 2 == 0
            breedRepository.save(
                    Breed.builder()
                            .breedId('breedId' + idx)
                            .category(isEven ? '가' : '나')
                            .breedName((isEven ? '골든 리드리버' : '나로 시작하는 견종') + idx)
                            .animalType(AnimalType.DOG)
                            .build()
            )
        })

        expect:
        breedRepository.count() == count

        where:
        count << [0, 10]
    }
}
