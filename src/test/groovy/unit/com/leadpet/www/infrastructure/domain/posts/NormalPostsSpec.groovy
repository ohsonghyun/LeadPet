package com.leadpet.www.infrastructure.domain.posts

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.NormalPostsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * NormalPostsSpec
 */
@DataJpaTest
@Import(TestConfig.class)
@Transactional
class NormalPostsSpec extends Specification {

    @PersistenceContext
    EntityManager em;

    @Autowired
    NormalPostsRepository normalPostsRepository

    def "일반 게시글 추가"() {
        when:
        normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(postId)
                        .title(title)
                        .contents(contents)
                        .build()
        )
        em.flush()
        em.clear()

        then:
        final saved = normalPostsRepository.findById(postId).orElseThrow()
        saved != null
        saved.getNormalPostId() == postId
        saved.getTitle() == title

        where:
        postId     | title   | contents
        'NP_dummy' | 'title' | 'contents'
    }

    def "일반 게시글을 수정"() {
        given:
        final target = normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(postId)
                        .title(title)
                        .contents(contents)
                        .build()
        )

        when:
        target.update(
                NormalPosts.builder()
                        .title(newTitle)
                // .contents(contents) // null 은 업데이트 대상외
                        .images(images)
                        .build())
        em.flush()
        em.clear()

        then:
        final result = normalPostsRepository.findById(postId).orElseThrow()
        result.getNormalPostId() == postId
        result.getContents() == contents
        result.getImages() == images

        where:
        postId     | title   | contents   | newTitle   | images
        'NP_dummy' | 'title' | 'contents' | 'newTitle' | ['img1', 'img2']
    }

    def "일반 게시글 삭제"() {
        given:
        normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(postId)
                        .title(title)
                        .contents(contents)
                        .build()
        )
        em.flush()
        em.clear()

        when:
        normalPostsRepository.deleteById(postId)

        then:
        final saved = normalPostsRepository.findById(postId)
        saved.isEmpty()

        where:
        postId     | title   | contents
        'NP_dummy' | 'title' | 'contents'
    }
}
