package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.AdoptionPostsRepository
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * AdoptionPostsSpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class AdoptionPostsSpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    AdoptionPostsRepository adoptionPostsRepository

    def "입양 피드 추가"() {
        when:
        adoptionPostsRepository.save(
                AdoptionPosts.builder()
                        .adoptionPostId(postId)
                        .startDate(startDate)
                        .endDate(endDate)
                        .euthanasiaDate(endDate)
                        .title(title)
                        .contents(contents)
                        .animalType(animalType)
                        .species(species)
                        .gender(gender)
                        .neutering(neutering)
                        .images(images)
                        .build()
        )

        em.flush()
        em.clear()

        then:
        final saved = adoptionPostsRepository.findById(postId).orElseThrow()
        saved.getAdoptionPostId() == postId
        saved.getTitle() == title
        saved.getContents() == contents
        saved.getAnimalType() == animalType
        saved.getGender() == gender
        saved.getSpecies() == species
        saved.getNeutering() == neutering
        saved.getImages() == images
        saved.getStartDate() == startDate
        saved.getEndDate() == endDate
        saved.getEuthanasiaDate() == endDate

        where:
        postId   | title   | contents   | animalType     | species   | gender      | neutering     | images           | startDate           | endDate
        'postId' | 'title' | 'contents' | AnimalType.DOG | 'species' | Gender.MALE | Neutering.YES | ['img1', 'img2'] | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

    def "입양 피드 페이지네이션: 데이터가 있는 경우"() {
        given:
        for (int i = 0; i < numOfPosts; i++) {
            adoptionPostsRepository.save(
                    AdoptionPosts.builder()
                            .adoptionPostId('postId' + i)
                            .startDate(startDate)
                            .endDate(endDate)
                            .euthanasiaDate(endDate.plusDays(i))
                            .title('title')
                            .contents('contents')
                            .animalType(AnimalType.DOG)
                            .species('species')
                            .gender(Gender.MALE)
                            .neutering(Neutering.YES)
                            .images(['img1', 'img2'])
                            .build()

            )
        }

        em.flush()
        em.clear()

        when:
        def result = adoptionPostsRepository.searchAll(PageRequest.of(0, 5))

        then:
        result.getContent().size() == 5
        result.getContent().get(0).getEuthanasiaDate().isBefore(result.getContent().get(1).getEuthanasiaDate())
        result.getTotalElements() == numOfPosts

        where:
        numOfPosts | startDate           | endDate
        20         | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

    def "입양 피드 페이지네이션: 데이터가 없는 경우"() {
        when:
        def result = adoptionPostsRepository.searchAll(PageRequest.of(0, 5))

        then:
        result.getContent().size() == 0
        result.getTotalPages() == 0
        result.getTotalElements() == 0
    }

}
