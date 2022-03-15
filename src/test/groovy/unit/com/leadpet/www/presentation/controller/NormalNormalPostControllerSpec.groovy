package com.leadpet.www.presentation.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.leadpet.www.infrastructure.db.NormalPostsRepository
import com.leadpet.www.infrastructure.db.UsersRepository
import com.leadpet.www.infrastructure.domain.posts.NormalPosts
import com.leadpet.www.infrastructure.domain.users.LoginMethod
import com.leadpet.www.infrastructure.domain.users.UserType
import com.leadpet.www.infrastructure.domain.users.Users
import com.leadpet.www.presentation.dto.request.post.AddNormalPostRequestDto
import com.leadpet.www.presentation.dto.request.post.UpdateNormalPostRequestDto
import org.hamcrest.Matchers
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * NormalPostControllerSpec
 */
@SpringBootTest
class NormalNormalPostControllerSpec extends Specification {
    private final String NORMAL_POST_URL = "/v1/post/normal"

    @Autowired
    WebApplicationContext webApplicationContext
    @Autowired
    UsersRepository usersRepository
    @Autowired
    NormalPostsRepository normalPostsRepository
    MockMvc mvc

    def setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build()
    }

    def cleanup() {
        normalPostsRepository.deleteAll()
        usersRepository.deleteAll()
    }

    def "모든 일반게시물 데이터를 취득한다"() {
        given:
        Users user = addNewUser(LoginMethod.KAKAO, 'uid', 'name', UserType.NORMAL)
        normalPostsRepository.saveAll(List.of(
                NormalPosts.builder().normalPostId("NP_a").title("title").contents("contents").user(user).build(),
                NormalPosts.builder().normalPostId("NP_b").title("title").contents("contents").user(user).build(),
                NormalPosts.builder().normalPostId("NP_c").title("title").contents("contents").user(user).build()
        ))

        expect:
        mvc.perform(get(NORMAL_POST_URL + "/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$', Matchers.hasSize(3)))
    }

    def "일반 게시물 추가: 정상"() {
        given:
        addNewUser(LoginMethod.KAKAO, uid, 'name', UserType.NORMAL)
        AddNormalPostRequestDto addNormalPostRequestDto = AddNormalPostRequestDto.builder()
                .title(title)
                .contents(contents)
                .images(images)
                .tags(tags)
                .userId(userId)
                .build()

        expect:
        mvc.perform(post(NORMAL_POST_URL + '/add')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addNormalPostRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.title').value(title))
                .andExpect(jsonPath('$.contents').value(contents))
                .andExpect(jsonPath('$.images').value(images))
                .andExpect(jsonPath('$.tags').value(tags))
                .andExpect(jsonPath('$.userId').isNotEmpty())

        where:
        title   | contents   | images           | tags                     | uid   | userId
        'title' | 'contents' | ['img1', 'img2'] | ['tag1', 'tag2', 'tag3'] | 'uid' | 'uidkko'
    }

    def "일반 게시물 추가: 에러: 404 - 존재하지 않는 유저"() {
        given:
        AddNormalPostRequestDto addNormalPostRequestDto = AddNormalPostRequestDto.builder()
                .title('title')
                .contents('contents')
                .userId('uidkko')
                .build()

        expect:
        mvc.perform(post(NORMAL_POST_URL + '/add')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(addNormalPostRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value('Error: 존재하지 않는 유저'))
    }

    def "일반 게시물 수정: 정상"() {
        given:
        // 유저 생성
        Users user = addNewUser(loginMethod, uid, 'name', UserType.NORMAL)
        String userId = user.getUserId()

        // 기존 일반 게시글 생성
        NormalPosts existingPost = addNormalPost('NP_a', 'title', 'contents', user)
        String normalPostId = existingPost.getNormalPostId()

        UpdateNormalPostRequestDto updateNormalPostRequestDto = UpdateNormalPostRequestDto.builder()
                .normalPostId(normalPostId)
                .title(updatedTitle)
                .contents(updatedContents)
                .images(updatedImages)
                .tags(updatedTags)
                .userId(userId)
                .build()

        expect:
        mvc.perform(put(NORMAL_POST_URL + '/update')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateNormalPostRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.normalPostId').value(normalPostId))
                .andExpect(jsonPath('$.title').value(updatedTitle))
                .andExpect(jsonPath('$.contents').value(updatedContents))
                .andExpect(jsonPath('$.images').value(updatedImages))
                .andExpect(jsonPath('$.tags').value(updatedTags))
                .andExpect(jsonPath('$.userId').value(userId))

        NormalPosts updatedPost = normalPostsRepository.findById(normalPostId).get()
        updatedPost != null
        updatedPost.title == updatedTitle
        updatedPost.contents == updatedContents
        updatedPost.images == updatedImages
        updatedPost.tags == updatedTags

        where:
        testCase | uid   | loginMethod       | updatedTitle   | updatedContents   | updatedImages            | updatedTags
        '정상'     | 'uid' | LoginMethod.KAKAO | 'updatedTitle' | 'updatedContents' | ['updated1', 'updated2'] | ['updated1', 'updated2']
    }

    def "일반 게시물 수정: 404 - #error"() {
        given:
        // 유저 생성
        Users user = addNewUser(loginMethod, uid, 'name', UserType.NORMAL)
        UpdateNormalPostRequestDto updateNormalPostRequestDto = UpdateNormalPostRequestDto.builder()
                .normalPostId(normalPostId)
                .title(updatedTitle)
                .contents(updatedContents)
                .images(updatedImages)
                .tags(updatedTags)
                .userId(user.getUserId())
                .build()

        expect:
        mvc.perform(put(NORMAL_POST_URL + '/update')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateNormalPostRequestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath('\$.error.detail').value(error))

        where:
        error                | uid   | normalPostId | loginMethod       | updatedTitle   | updatedContents   | updatedImages            | updatedTags
        'Error: 존재하지 않는 게시글' | 'uid' | 'NP_a'       | LoginMethod.KAKAO | 'updatedTitle' | 'updatedContents' | ['updated1', 'updated2'] | ['updated1', 'updated2']
    }

    def "일반 게시물 수정: 403 - #error"() {
        given:
        // 유저 생성
        Users userA = addNewUser(loginMethod, 'uidA', 'name', UserType.NORMAL)
        Users userB = addNewUser(loginMethod, 'uidB', 'name', UserType.NORMAL)
        // 일반 게시글 추가한 유저는 userA
        NormalPosts existingPost = addNormalPost(normalPostId, 'title', 'contents', userA)
        // 일반 게시글 수정쿼리의 userId는 userB: 권한 없는 조작
        UpdateNormalPostRequestDto updateNormalPostRequestDto = UpdateNormalPostRequestDto.builder()
                .normalPostId(normalPostId)
                .title('updatedTitle')
                .contents('updatedContents')
                .images(['updated1', 'updated2'])
                .tags(['updated1', 'updated2'])
                .userId(userB.getUserId())
                .build()

        expect:
        mvc.perform(put(NORMAL_POST_URL + '/update')
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateNormalPostRequestDto)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath('\$.error.detail').value(error))

        where:
        error             | normalPostId | loginMethod
        'Error: 권한 없는 조작' | 'NP_a'       | LoginMethod.KAKAO
    }


    // TODO 일반 게시물 삭제
    def "일반 게시물 삭제: 정상"() {

    }

    // TODO 일반 게시물 삭제
    def "일반 게시물 삭제: 에러: 404-존재하지 않는 게시글"() {

    }

    // ----------------------------------------------------

    /**
     * Users 등록
     */
    private Users addNewUser(LoginMethod loginMethod, String uid, String name, UserType userType) {
        Users user = Users.builder()
                .loginMethod(loginMethod)
                .uid(uid)
                .name(name)
                .userType(userType)
                .build()
        user.createUserId()
        Users result = usersRepository.save(user)
        usersRepository.flush()
        return result
    }

    /**
     * 일반 게시글 등록
     */
    private NormalPosts addNormalPost(String normalPostId, String title, String contents, Users user) {
        return normalPostsRepository.save(
                NormalPosts.builder()
                        .normalPostId(normalPostId)
                        .title(title)
                        .contents(contents)
                        .user(user)
                        .build())

    }

}
