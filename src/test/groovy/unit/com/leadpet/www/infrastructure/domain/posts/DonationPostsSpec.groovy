package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.infrastructure.db.DonationPostsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import java.time.LocalDateTime

/**
 * DonationPostsSpec
 */
@DataJpaTest
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
        postId   | title   | contents   | images           | startDate           | endDate
        'postId' | 'title' | 'contents' | ['img1', 'img2'] | LocalDateTime.now() | LocalDateTime.now().plusDays(10)
    }
}
