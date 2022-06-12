package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.DonationPostsRepository
import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.DonationPosts
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.infrastructure.exception.login.UserNotFoundException
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * DonationPostServiceSpec
 */
class DonationPostServiceSpec extends Specification {

    private UsersRepository usersRepository
    private DonationPostsRepository donationPostsRepository
    private DonationPostService donationPostService

    def setup() {
        usersRepository = Mock(UsersRepository)
        donationPostsRepository = Mock(DonationPostsRepository)
        donationPostService = new DonationPostService(donationPostsRepository, usersRepository)
    }

    def "DonationPosts을 추가"() {
        given:
        1 * usersRepository.findById(_ as String) >> Optional.of(
                Users.builder()
                        .userId(userId)
                        .build())
        1 * donationPostsRepository.save(_ as DonationPosts) >> DonationPosts.builder()
                .donationPostId(postId)
                .startDate(startDate)
                .endDate(endDate)
                .title(title)
                .donationMethod(donationMethod)
                .contents(contents)
                .images(images)
                .user(
                        Users.builder()
                                .userId(userId)
                                .build())
                .build()

        when:
        // DonationPosts 를 DB에 저장
        final DonationPosts result = donationPostService.addNewPost(
                DonationPosts.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .title(title)
                        .donationMethod(donationMethod)
                        .contents(contents)
                        .images(images)
                        .build()
                , userId)

        then:
        result != null
        result instanceof DonationPosts
        result.getDonationPostId() == postId
        result.getStartDate() == startDate
        result.getEndDate() == endDate
        result.getTitle() == title
        result.getDonationMethod() == donationMethod
        result.getContents() == contents
        result.getImages() == images
        result.getUser() != null
        result.getUser().getUserId() == userId

        where:
        userId        | postId        | startDate           | endDate               | title        | donationMethod        | contents       | images
        'dummyUserId' | 'dummyPostId' | LocalDateTime.now() | startDate.plusDays(5) | 'dummyTitle' | 'dummyDonationMethod' | 'dummyContent' | ['img1', 'img2']
    }

    def "userId가 존재하지 않으면 에러"() {
        given:
        usersRepository.findById(_ as String) >> Optional.empty()

        when:
        donationPostService.addNewPost(
                DonationPosts.builder()
                        .startDate(startDate)
                        .endDate(endDate)
                        .title(title)
                        .donationMethod(donationMethod)
                        .contents(contents)
                        .images(images)
                        .build(),
                userId
        )

        then:
        thrown(UserNotFoundException)

        where:
        userId   | startDate           | endDate               | title        | donationMethod        | contents       | images
        'userId' | LocalDateTime.now() | startDate.plusDays(5) | 'dummyTitle' | 'dummyDonationMethod' | 'dummyContent' | ['img1', 'img2']
    }

}
