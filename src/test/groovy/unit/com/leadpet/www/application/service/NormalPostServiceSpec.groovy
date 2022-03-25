package com.leadpet.www.application.service

import com.leadpet.www.infrastructure.db.NormalPostsRepository
import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.Users
import spock.lang.Specification

/**
 * NormalPostServiceSpec
 */
class NormalPostServiceSpec extends Specification {

    private NormalPostService normalPostService
    private NormalPostsRepository normalPostsRepository
    private UsersRepository usersRepository

    def setup() {
        this.usersRepository = Mock(UsersRepository.class)
        this.normalPostsRepository = Mock(NormalPostsRepository.class)
        this.normalPostService = new NormalPostService(usersRepository, normalPostsRepository)
    }

    def "모든 일반게시물을 취득한다"() {
        given:
        normalPostsRepository.findAll() >> [
                NormalPosts.builder().normalPostId('NP_a').title('title').contents('contents').images(['img1', 'img2']).tags(['tag1', 'tag2']).user(Users.builder().userId('userId').build()).build(),
                NormalPosts.builder().normalPostId('NP_b').title('title').contents('contents').images(['img1', 'img2']).tags(['tag1', 'tag2']).user(Users.builder().userId('userId').build()).build(),
                NormalPosts.builder().normalPostId('NP_c').title('title').contents('contents').images(['img1', 'img2']).tags(['tag1', 'tag2']).user(Users.builder().userId('userId').build()).build()
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
        normalPost.getUser().getUserId() == 'userId'
    }

}
