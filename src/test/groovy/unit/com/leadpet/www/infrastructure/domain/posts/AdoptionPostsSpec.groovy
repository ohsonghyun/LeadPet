package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.adoptionPost.AdoptionPostsRepository
import com.leadpet.www.infrastructure.db.adoptionPost.condition.SearchAdoptionPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime
import java.util.stream.IntStream

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
    UsersRepository usersRepository

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
        postId   | title   | contents   | animalType     | species   | gender      | neutering     | images           | startDate                       | endDate
        'postId' | 'title' | 'contents' | AnimalType.DOG | 'species' | Gender.MALE | Neutering.YES | ['img1', 'img2'] | LocalDateTime.now().withNano(0) | LocalDateTime.now().plusDays(10).withNano(0)
    }

}
