package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.infrastructure.db.AdoptionPostsRepository
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * AdoptionPostsSpec
 */
@DataJpaTest
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
        postId   | title   | contents   | animalType   | species   | gender      | neutering     | images           | startDate           | endDate
        'postId' | 'title' | 'contents' | 'animalType' | 'species' | Gender.MALE | Neutering.YES | ['img1', 'img2'] | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }
}
