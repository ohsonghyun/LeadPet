package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.donationPost.DonationPostsRepository
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
 * DonationPostsSpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class DonationPostsSpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    DonationPostsRepository donationPostsRepository

    def "기부 피드 추가"() {
        when:
        donationPostsRepository.save(
                DonationPosts.builder()
                        .donationPostId(postId)
                        .title(title)
                        .contents(contents)
                        .images(images)
                        .startDate(startDate)
                        .endDate(endDate)
                        .build())

        em.flush()
        em.clear()

        then:
        final saved = donationPostsRepository.findById(postId).orElseThrow()
        saved.getDonationPostId() == postId
        saved.getTitle() == title
        saved.getContents() == contents
        saved.getImages() == images
        saved.getStartDate() == startDate
        saved.getEndDate() == endDate

        where:
        postId   | title   | contents   | images           | startDate                       | endDate
        'postId' | 'title' | 'contents' | ['img1', 'img2'] | LocalDateTime.now().withNano(0) | LocalDateTime.now().plusDays(10).withNano(0)
    }

    def "기부 피드 페이지네이션: 데이터가 있는 경우"() {
        given:
        for (int i = 0; i < numOfPosts; i++) {
            donationPostsRepository.save(
                    DonationPosts.builder()
                            .donationPostId('postId' + i)
                            .title('title')
                            .contents('contents')
                            .images(['img1', 'img2'])
                            .startDate(startDate)
                            .endDate(endDate)
                            .build())
        }

        em.flush()
        em.clear()

        when:
        final result = donationPostsRepository.searchAll(PageRequest.of(0, 5))

        then:
        result.getContent().size() == 5
        result.getTotalElements() == numOfPosts

        where:
        numOfPosts | now                 | startDate        | endDate
        20         | LocalDateTime.now() | now.minusDays(1) | now.plusDays(10)
    }

    def "기부 피드 페이지네이션: 데이터가 없는 경우"() {
        when:
        final result = donationPostsRepository.searchAll(PageRequest.of(0, 5))

        then:
        result.getContent().size() == 0
        result.getTotalElements() == 0
    }

}
