package com.leadpet.www.application.service.liked

import com.leadpet.www.infrastructure.db.liked.LikedRepository
import com.leadpet.www.infrastructure.domain.liked.Liked
import com.leadpet.www.infrastructure.domain.liked.LikedResult
import spock.lang.Specification

/**
 * LikedServiceSpec
 */
class LikedServiceSpec extends Specification {

    private LikedRepository likedRepository
    private LikedService likedService

    def setup() {
        likedRepository = Mock(LikedRepository)
        likedService = new LikedService(likedRepository)
    }

    def "[데이터 갱신] #testcase"() {
        given:
        likedRepository.findByUserIdAndPostId(_ as String, _ as String) >> liked

        when:
        def result = likedService.saveOrDelete(userId, postId)

        then:
        saveNumberOfCount * likedRepository.save(_ as Liked)
        deleteNumberOfCount * likedRepository.deleteByUserIdAndPostId(userId, postId)
        result == expectedResult

        where:
        testcase           | userId   | postId   | liked                     | saveNumberOfCount | deleteNumberOfCount | expectedResult
        '좋아요 데이터가 없는 경우'   | 'userId' | 'postId' | null                      | 1                 | 0                   | LikedResult.CREATED
        '좋아요 데이터가 존재하는 경우' | 'userId' | 'postId' | likedData(userId, postId) | 0                 | 1                   | LikedResult.DELETED
    }

    /**
     * Liked 데이터 생성
     */
    def likedData(userId, postId) {
        return Liked.builder()
                .likedId('likedId')
                .userId(userId)
                .postId(postId)
                .build()
    }
}
