package com.leadpet.www.infrastructure.db.liked

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.domain.liked.Liked
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


/**
 * LikedRepositorySpec
 */
@DataJpaTest
@Import(TestConfig)
@Transactional
class LikedRepositorySpec extends Specification {

    @PersistenceContext
    private EntityManager em

    @Autowired
    LikedRepository likedRepository

    def "[좋아요]를 기록할 수 있다"() {
        given:
        def liked = Liked.builder()
                .likedId('likedId')
                .userId('userId')
                .postId('postId')
                .build()

        when:
        likedRepository.save(liked)
        em.flush()
        em.clear()

        then:
        def allLiked = likedRepository.findAll()
        allLiked.size() == 1

        def resultLiked = allLiked.get(0)
        resultLiked.getLikedId() == liked.getLikedId()
        resultLiked.getUserId() == liked.getUserId()
        resultLiked.getPostId() == liked.getPostId()
    }

    def "[좋아요]를 삭제할 수 있다"() {
        given:
        likedRepository.save(
                Liked.builder()
                        .likedId(likedId)
                        .userId(userId)
                        .postId(postId)
                        .build()
        )
        em.flush()
        em.clear()

        when:
        likedRepository.deleteByUserIdAndPostId(userId, postId)
        em.flush()
        em.clear()

        then:
        def deletedLiked = likedRepository.findById(likedId)
        deletedLiked.isEmpty()

        where:
        likedId   | userId   | postId
        'likedId' | 'userId' | 'postId'
    }

}
