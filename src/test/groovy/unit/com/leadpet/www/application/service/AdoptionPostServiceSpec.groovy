package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.AdoptionPostsRepository
import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.pet.AnimalType
import com.leadpet.www.infrastructure.domain.pet.Gender
import com.leadpet.www.infrastructure.domain.pet.Neutering
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import com.leadpet.www.presentation.dto.response.post.adoption.AddAdoptionPostResponseDto
import com.leadpet.www.presentation.dto.response.post.adoption.AdoptionPostPageResponseDto
import org.springframework.data.domain.Page
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
                .species(species)
                .gender(gender)
                .neutering(neutering)
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
                        .species(species)
                        .gender(gender)
                        .neutering(neutering)
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
        result.getSpecies() == species
        result.getNeutering() == neutering
        result.getImages() == images
        result.getStartDate() == startDate
        result.getEndDate() == endDate
        result.getEuthanasiaDate() == endDate
        result.getUser() != null
        result.getUser().getUserId() == userId

        where:
        postId   | userId   | title   | contents   | animalType     | species   | gender      | neutering     | images           | startDate           | endDate
        'postId' | 'userId' | 'title' | 'contents' | AnimalType.DOG | 'species' | Gender.MALE | Neutering.YES | ['img1', 'img2'] | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
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
                        .species(species)
                        .gender(gender)
                        .neutering(neutering)
                        .images(images)
                        .build(),
                userId
        )

        then:
        thrown(UserNotFoundException)

        where:
        postId   | userId   | title   | contents   | animalType     | species   | gender      | neutering     | images           | startDate           | endDate
        'postId' | 'userId' | 'title' | 'contents' | AnimalType.DOG | 'species' | Gender.MALE | Neutering.YES | ['img1', 'img2'] | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

    def "입양 피드 검색(pagination)"() {
        given:
        // 입양 포스트 5개 생성
        List<AddAdoptionPostResponseDto> content = new ArrayList<>()
        for (int i = 0; i < 5; i++) {
            content.add(
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
        adoptionPostsRepository.searchAll(_ as Pageable) >> new PageImpl<AdoptionPostPageResponseDto>(content, pageRequest, totalSize)

        when:
        final result = adoptionPostService.searchAll(pageRequest)

        then:
        result != null
        result.getContent().size() == 5
        result.getTotalElements() == 20

        where:
        pageRequest          | totalSize | startDate           | endDate
        PageRequest.of(0, 5) | 20        | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }

}
