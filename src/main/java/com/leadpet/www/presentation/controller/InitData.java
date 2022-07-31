package com.leadpet.www.presentation.controller;

import com.leadpet.www.infrastructure.domain.breed.Breed;
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts;
import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply;
import com.leadpet.www.infrastructure.domain.users.AssessmentStatus;
import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

/**
 * InitData
 * <p>테스트용 데이터 작성 클래스</p>
 */
@Profile("local")
@Component
@lombok.RequiredArgsConstructor
public class InitData {
    private final InitNormalUserService initNormalUserService;
    private final InitShelterService initShelterService;
    private final InitPostService initPostService;
    private final InitBreedService initBreedService;
    private final InitNormalReplyService initNormalReplyService;

    @PostConstruct
    public void init() {
        initShelterService.init();
        initNormalUserService.init();
        initPostService.init();
        initBreedService.init();
        initNormalReplyService.init();
    }

    @Component
    static class InitNormalUserService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            List<String> names = List.of("김땡땡", "박뫄뫄", "곽붕붕");
            // KAKAO
            IntStream.range(101, 130).forEach(idx -> {
                final String name = names.get((int) (Math.random() * names.size())) + idx;
                em.persist(
                        Users.builder()
                                .userId("uidkko" + idx)
                                .loginMethod(LoginMethod.KAKAO)
                                .name(name)
                                .uid("uid" + idx)
                                .userType(UserType.NORMAL)
                                .build());
            });

            // EMAIL
            IntStream.range(131, 160).forEach(idx -> {
                final String name = names.get((int) (Math.random() * names.size())) + idx;
                em.persist(
                        Users.builder()
                                .userId("uidkko" + idx)
                                .loginMethod(LoginMethod.EMAIL)
                                .name(name)
                                .uid("uid" + idx)
                                .userType(UserType.NORMAL)
                                .email("email" + idx + "@email.com")
                                .password("password")
                                .build());
            });
        }
    }

    @Component
    static class InitShelterService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            List<String> names = List.of("헐크", "아이언맨", "토르", "블랙위도우", "캡틴아메리카", "호크아이", "스파이더맨");
            List<String> cities = List.of("서울시", "부산시", "대전시", "대구시", "울산시", "광주시", "인천시");
            IntStream.range(0, 100).forEach(idx -> {
                final String name = names.get((int) (Math.random() * names.size())) + idx;
                final String city = cities.get((int) (Math.random() * cities.size()));
                em.persist(
                        Users.builder()
                                .userId("app" + idx)
                                .loginMethod(LoginMethod.APPLE)
                                .uid("uid" + idx)
                                .name(name)
                                .userType(UserType.SHELTER)
                                .shelterName(name + " 보호소")
                                .shelterAddress(city + " 헬로우 월드 주소 어디서나 123-123")
                                .shelterAssessmentStatus(AssessmentStatus.COMPLETED)
                                .build());
            });
        }
    }

    @Component
    static class InitPostService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            List<Users> allUsers = em.createQuery("select u from Users u").getResultList();
            for (Users user : allUsers) {
                for (int i = 0; i < 3; i++) {
                    em.persist(
                            NormalPosts.builder()
                                    .normalPostId("NP_" + user.getUserId() + i)
                                    .title("title" + user.getUserId() + i)
                                    .contents("contents" + user.getUserId() + i)
                                    .user(user)
                                    .build());

                    // 보호소만 기부/입양 피드 작성
                    // TODO 이거 로직에서도 추가해야하지 않나?

                    if (user.getUserType() == UserType.SHELTER) {
                        em.persist(
                                DonationPosts.builder()
                                        .donationPostId("DP_" + user.getUserId() + i)
                                        .title("title" + user.getUserId() + i)
                                        .contents("contents" + user.getUserId() + i)
                                        .startDate(LocalDateTime.now().minusDays(1))
                                        .endDate(LocalDateTime.now().plusDays((int) (Math.random() * 10)))
                                        .user(user)
                                        .build());
                        em.persist(
                                AdoptionPosts.builder()
                                        .adoptionPostId("AP_" + user.getUserId() + i)
                                        .title("title" + user.getUserId() + i)
                                        .contents("contents" + user.getUserId() + i)
                                        .user(user)
                                        .build());
                    }
                }
            }
        }
    }

    @Component
    static class InitBreedService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            // 좀 더 좋은 코드로 바꾸자.. 지금은 졸리니까 기백으로 밀어 붙인다
            // 가
            em.persist(
                    Breed.builder()
                            .breedId("breedId0")
                            .category("가")
                            .breedName("골든 리트리버")
                            .build());
            em.persist(
                    Breed.builder()
                            .breedId("breedId1")
                            .category("가")
                            .breedName("고든셰터")
                            .build());
            // 사
            em.persist(
                    Breed.builder()
                            .breedId("breedId2")
                            .category("사")
                            .breedName("시츄")
                            .build());
            em.persist(
                    Breed.builder()
                            .breedId("breedId3")
                            .category("사")
                            .breedName("스피츠")
                            .build());
            em.persist(
                    Breed.builder()
                            .breedId("breedId4")
                            .category("사")
                            .breedName("시바")
                            .build());

        }
    }

    @Component
    static class InitNormalReplyService {

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            List<NormalPosts> normalPosts = em.createQuery("select p from NormalPosts p").getResultList();

            for (NormalPosts normalPost : normalPosts) {
                int randomNum = (int) (Math.random() * 6) + 1;
                IntStream.range(0, randomNum).forEach(idx -> {
                    em.persist(
                            NormalReply.builder()
                                    .normalReplyId("NR_" + RandomStringUtils.random(10, true, true))
                                    .user(normalPost.getUser())
                                    .content("dummy content")
                                    .normalPost(normalPost)
                                    .build());
                });
            }
        }

    }
}
