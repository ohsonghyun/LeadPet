package com.leadpet.www.application.service.breed

import com.leadpet.www.infrastructure.db.breed.BreedRepository
import com.leadpet.www.infrastructure.domain.breed.Breed
import spock.lang.Specification

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
                .build()

        when:
        def savedBreed = breedService.save(category, breedName)

        then:
        savedBreed != null
        savedBreed.getBreedId() == breedId
        savedBreed.getCategory() == category
        savedBreed.getBreedName() == breedName

        where:
        breedId   | category | breedName
        'breedId' | '가'      | '골든 리트리버'
    }

    def "품종 리스트 취득"() {
        given:
        breedRepository.findAll() >> List.of(
                new Breed('breedId1', '가', '골든 리트리버'),
                new Breed('breedId2', '가', '고든 셰터'),
                new Breed('breedId3', '사', '스피츠'),
                new Breed('breedId4', '사', '시츄'),
                new Breed('breedId5', '사', '시바')
        )

        when:
        Map<String, List<String>> result = breedService.findGroupByCategory()

        then:
        result.get('가').size() == 2
        result.get('가').get(0) == '골든 리트리버'
        result.get('가').get(1) == '고든 셰터'

        result.get('사').size() == 3
        result.get('사').get(0) == '스피츠'
        result.get('사').get(1) == '시츄'
        result.get('사').get(2) == '시바'
    }
}
