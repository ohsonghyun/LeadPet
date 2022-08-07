package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.donationPost.DonationPostsRepository
import com.leadpet.www.infrastructure.db.donationPost.condition.SearchDonationPostCondition
import com.leadpet.www.infrastructure.db.users.UsersRepository
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
 * DonationPostsSpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class DonationPostsSpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    UsersRepository usersRepository

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

}
