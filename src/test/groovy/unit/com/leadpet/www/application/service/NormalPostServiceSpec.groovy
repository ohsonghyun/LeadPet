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
                NormalPosts.builder().normalPostId(0).title('title').contents('contents').images(['img1', 'img2']).tags(['tag1', 'tag2']).userId(1).build(),
                NormalPosts.builder().normalPostId(1).title('title').contents('contents').images(['img1', 'img2']).tags(['tag1', 'tag2']).userId(1).build(),
                NormalPosts.builder().normalPostId(2).title('title').contents('contents').images(['img1', 'img2']).tags(['tag1', 'tag2']).userId(1).build()
        ]

        when:
        List<NormalPosts> normalPosts = normalPostService.getAllNormalPosts()

        then:
        normalPosts != null
        normalPosts.size() == 3
        NormalPosts normalPost = normalPosts.get(0)
        normalPost.getTitle() == 'title'
        normalPost.getContents() == 'contents'
        normalPost.getImages() == ['img1', 'img2']
        normalPost.getTags() == ['tag1', 'tag2']
        normalPost.getUserId() == 1
    }

}
