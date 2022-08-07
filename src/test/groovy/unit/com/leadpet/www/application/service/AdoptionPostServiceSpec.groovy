package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.adoptionPost.AdoptionPostsRepository
import com.leadpet.www.infrastructure.db.adoptionPost.condition.SearchAdoptionPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * AdoptionPostServiceSpec
 */
class AdoptionPostServiceSpec extends Specification {

    private UsersRepository usersRepository
    private AdoptionPostsRepository adoptionPostsRepository
    private AdoptionPostService adoptionPostService

    def setup() {
        usersRepository = Mock(UsersRepository)
        adoptionPostsRepository = Mock(AdoptionPostsRepository)
        adoptionPostService = new AdoptionPostService(adoptionPostsRepository, usersRepository)
    }

    def "AdoptionPosts를 추가"() {
        given:
        1 * usersRepository.findById(_ as String) >> Optional.of(
                Users.builder()
                        .userId(userId)
                        .build())
        1 * adoptionPostsRepository.save(_ as AdoptionPosts) >> AdoptionPosts.builder()
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
                .user(
                        Users.builder()
                                .userId(userId)
                                .build())
                .build()

        when:
        final AdoptionPosts result = adoptionPostService.addNewPost(
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
                , userId)

        then:
        result != null
        result instanceof AdoptionPosts
        result.getAdoptionPostId() == postId
        result.getTitle() == title
        result.getContents() == contents
        result.getAnimalType() == animalType
        result.getGender() == gender
        result.getBreed() == breed
        result.getNeutering() == neutering
        result.getAge() == age
        result.getDisease() == disease
        result.getImages() == images
        result.getStartDate() == startDate
        result.getEndDate() == endDate
        result.getEuthanasiaDate() == endDate
        result.getUser() != null
        result.getUser().getUserId() == userId

        where:
        postId   | userId   | title   | contents   | animalType     | breed   | gender      | neutering     | age | disease     | images           | startDate           | endDate
        'postId' | 'userId' | 'title' | 'contents' | AnimalType.DOG | 'breed' | Gender.MALE | Neutering.YES |  1  | 'disease'   | ['img1', 'img2'] | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

    def "userId가 존재하지 않으면 에러"() {
        given:
        usersRepository.findById(_ as String) >> Optional.empty()

        when:
        adoptionPostService.addNewPost(
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
                        .build(),
                userId
        )

        then:
        thrown(UserNotFoundException)

        where:
        postId   | userId   | title   | contents   | animalType     | breed   | gender      | neutering     | age | disease     | images           | startDate           | endDate
        'postId' | 'userId' | 'title' | 'contents' | AnimalType.DOG | 'breed' | Gender.MALE | Neutering.YES |  1  | 'disease'   | ['img1', 'img2'] | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

    def "입양 피드 검색(pagination)"() {
        given:
        // 입양 포스트 5개 생성
        List<AdoptionPostPageResponseDto> content = new ArrayList<>()
        for (int i = 0; i < 5; i++) {
            content.add(
                    AdoptionPostPageResponseDto.builder()
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
                            .userId('userId' + i)
                            .build()
            )
        }
        adoptionPostsRepository.searchAll(_ as SearchAdoptionPostCondition, _ as Pageable) >> new PageImpl<AdoptionPostPageResponseDto>(content, pageRequest, totalSize)

        when:
        final result = adoptionPostService.searchAll(SearchAdoptionPostCondition.builder().build(), pageRequest)

        then:
        result != null
        result.getContent().size() == 5
        result.getTotalElements() == totalSize

        where:
        pageRequest          | totalSize | startDate           | endDate
        PageRequest.of(0, 5) | 20        | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

}
