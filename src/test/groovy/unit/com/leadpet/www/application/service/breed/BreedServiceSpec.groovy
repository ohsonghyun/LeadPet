package com.leadpet.www.application.service.breed

import com.leadpet.www.infrastructure.db.breed.BreedRepository
import com.leadpet.www.infrastructure.domain.breed.Breed
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.presentation.dto.response.breed.SearchBreedResponse
import com.leadpet.www.presentation.dto.response.breed.SearchBreedResultResponse
import org.assertj.core.api.Assertions
import org.assertj.core.api.ObjectAssert
import spock.lang.Specification
import spock.lang.Unroll

/**
 * BreedServiceSpec
 */
class BreedServiceSpec extends Specification {
    private BreedRepository breedRepository
    private BreedService breedService

    def setup() {
        breedRepository = Mock(BreedRepository)
        breedService = new BreedService(breedRepository)
    }

    def "품종 등록"() {
        given:
        breedRepository.save(_ as Breed) >> Breed.builder()
                .breedId(breedId)
                .category(category)
                .breedName(breedName)
                .animalType(animalType)
                .build()

        when:
        def savedBreed = breedService.save(category, breedName)

        then:
        savedBreed != null
        savedBreed.getBreedId() == breedId
        savedBreed.getCategory() == category
        savedBreed.getBreedName() == breedName
        savedBreed.getAnimalType() == animalType

        where:
        breedId   | category | breedName | animalType
        'breedId' | '가'      | '골든 리트리버' | AnimalType.DOG
    }

    def "품종 리스트 취득"() {
        given:
        breedRepository.findAll() >> List.of(
                new Breed('breedId1', '가', '골든 리트리버', AnimalType.DOG),
                new Breed('breedId2', '가', '고든 셰터', AnimalType.DOG),
                new Breed('breedId3', '사', '스피츠', AnimalType.DOG),
                new Breed('breedId4', '사', '시츄', AnimalType.DOG),
                new Breed('breedId5', '사', '시바', AnimalType.DOG)
        )

        when:
        SearchBreedResultResponse result = breedService.findGroupByCategoryZz()

        then:
        {
            def it = Assertions.assertThat(result.getResults())
            it.hasSize(2)
            it.extractingResultOf('getIndex').containsExactly('가', '사')
            it.extractingResultOf('getBreedList').hasSize(2)
        }
        {
            def it = Assertions.assertThat(result.getResults().get(0).getBreedList())
            it.hasSize(2)
            it.extracting('breedName').containsExactly('골든 리트리버', '고든 셰터')
        }
        {
            def it = Assertions.assertThat(result.getResults().get(1).getBreedList())
            it.hasSize(3)
            it.extracting('breedName').containsExactly('스피츠', '시츄', '시바')
        }
    }

    @Unroll
    def "전체 카운트 취득"() {
        given:
        breedRepository.count() >> count

        expect:
        breedService.count() == count

        where:
        count << [0L, 10L, 100L]
    }
}
