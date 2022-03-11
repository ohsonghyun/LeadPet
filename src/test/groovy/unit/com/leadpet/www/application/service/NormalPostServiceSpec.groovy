package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.NormalPostsRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import spock.lang.Specification

/**
 * NormalPostServiceSpec
 */
class NormalPostServiceSpec extends Specification {

    private NormalPostService normalPostService
    private NormalPostsRepository normalPostsRepository

    def setup() {
        this. normalPostsRepository = Mock(NormalPostsRepository.class)
        this.normalPostService = new NormalPostService(normalPostsRepository)
    }

    def "모든 일반게시물을 취득한다"() {
        given:
        normalPostsRepository.findAll() >> [
                NormalPosts.builder().normalPostId(0).title('title').contents('contents').build(),
                NormalPosts.builder().normalPostId(1).title('title').contents('contents').build(),
                NormalPosts.builder().normalPostId(2).title('title').contents('contents').build()
        ]

        expect:
        normalPostService.getAllNormalPosts().size() == 3
    }

}
