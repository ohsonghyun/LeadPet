package com.leadpet.www.infrastructure.db.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.posts.adoptionPost.AdoptionPostsRepository
import com.leadpet.www.infrastructure.db.posts.donationPost.DonationPostsRepository
import com.leadpet.www.infrastructure.db.posts.normalPost.NormalPostsRepository
import com.leadpet.www.infrastructure.domain.posts.PostType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import spock.lang.Specification
import spock.lang.Unroll

/**
 * PostUtilSpec
 */
@DataJpaTest
@Import(TestConfig)
class PostUtilSpec extends Specification {

    @Autowired
    private NormalPostsRepository normalPostsRepository
    @Autowired
    private DonationPostsRepository donationPostsRepository
    @Autowired
    private AdoptionPostsRepository adoptionPostsRepository
    private PostUtil postUtil

    def setup() {
        postUtil = new PostUtil(normalPostsRepository, donationPostsRepository, adoptionPostsRepository)
    }

    @Unroll("#testCase")
    def "피드타입으로 Repository 취득"() {
        when:
        def result = postUtil.getRepositoryBy(postType)

        then:
        result in expected

        where:
        testCase | postType               | expected
        "일상피드"   | PostType.NORMAL_POST   | NormalPostsRepository
        "기부피드"   | PostType.ADOPTION_POST | AdoptionPostsRepository
        "입양피드"   | PostType.DONATION_POST | DonationPostsRepository
    }

}
