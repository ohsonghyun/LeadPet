package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.posts.adoptionPost.AdoptionPostsRepository
import com.leadpet.www.infrastructure.db.posts.adoptionPost.condition.SearchAdoptionPostCondition
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
                        .breed(breed)
                        .gender(gender)
                        .neutering(neutering)
                        .age(age)
                        .disease(disease)
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
        saved.getBreed() == breed
        saved.getNeutering() == neutering
        saved.getAge() == age
        saved.getDisease() == disease
        saved.getImages() == images
        saved.getStartDate() == startDate
        saved.getEndDate() == endDate
        saved.getEuthanasiaDate() == endDate

        where:
        postId   | title   | contents   | animalType     | breed   | gender      | neutering     | age | disease   | images           | startDate                       | endDate
        'postId' | 'title' | 'contents' | AnimalType.DOG | 'breed' | Gender.MALE | Neutering.YES | 1   | 'disease' | ['img1', 'img2'] | LocalDateTime.now().withNano(0) | LocalDateTime.now().plusDays(10).withNano(0)
    }

    @Unroll("#testcase")
    def "입양 피드 페이지네이션: 데이터가 있는 경우"() {
        given:
        // 유저 app0, app1 추가
        IntStream.range(0, 2).forEach(idx -> {
            usersRepository.save(
                    Users.builder()
                            .userId("app" + idx)
                            .loginMethod(LoginMethod.APPLE)
                            .uid("uid" + idx)
                            .name('name' + idx)
                            .userType(UserType.SHELTER)
                            .shelterName("보호소" + idx)
                            .shelterAddress("헬로우 월드 주소 어디서나 123-123")
                            .shelterAssessmentStatus(AssessmentStatus.COMPLETED)
                            .build()
            )
        })

        // 피드 데이터 추가
        for (int i = 0; i < totalNumOfPosts; i++) {
            adoptionPostsRepository.save(
                    AdoptionPosts.builder()
                            .adoptionPostId('postId' + i)
                            .startDate(startDate)
                            .endDate(endDate)
                            .euthanasiaDate(endDate.plusDays(i))
                            .title('title')
                            .contents('contents')
                            .animalType(AnimalType.DOG)
                            .breed('breed')
                            .gender(Gender.MALE)
                            .neutering(Neutering.YES)
                            .age(1)
                            .disease('disease')
                            .images(['img1', 'img2'])
                            .user(usersRepository.findShelterByUserId(i % 2 ? 'app0' : 'app1'))
                            .build()
            )
        }

        em.flush()
        em.clear()

        when:
        def result = adoptionPostsRepository.searchAll(
                SearchAdoptionPostCondition.builder().userId(targetUserId).build(),
                PageRequest.of(0, 5))

        then:
        result.getContent().size() == 5
        result.getContent().get(0).getEuthanasiaDate().isBefore(result.getContent().get(1).getEuthanasiaDate())
        result.getTotalElements() == expectedNumOfPosts

        where:
        testcase          | totalNumOfPosts | startDate           | endDate                | targetUserId | expectedNumOfPosts
        '검색 userId가 공백'   | 20              | LocalDateTime.now() | startDate.plusDays(10) | ''           | 20
        '검색 userId가 null' | 20              | LocalDateTime.now() | startDate.plusDays(10) | null         | 20
        '검색 userId가 app0' | 20              | LocalDateTime.now() | startDate.plusDays(10) | 'app0'       | 10
        '검색 userId가 app1' | 20              | LocalDateTime.now() | startDate.plusDays(10) | 'app1'       | 10
    }

    def "입양 피드 페이지네이션: 데이터가 없는 경우"() {
        when:
        def result = adoptionPostsRepository.searchAll(SearchAdoptionPostCondition.builder().build(), PageRequest.of(0, 5))

        then:
        result.getContent().size() == 0
        result.getTotalPages() == 0
        result.getTotalElements() == 0
    }

}
