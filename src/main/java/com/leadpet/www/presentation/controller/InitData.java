package com.leadpet.www.presentation.controller;

import com.leadpet.www.infrastructure.domain.breed.Breed;
import com.leadpet.www.infrastructure.domain.donation.DonationMethod;
import com.leadpet.www.infrastructure.domain.liked.Liked;
import com.leadpet.www.infrastructure.domain.pet.AnimalType;
import com.leadpet.www.infrastructure.domain.posts.AdoptionPosts;
import com.leadpet.www.infrastructure.domain.posts.DonationPosts;
import com.leadpet.www.infrastructure.domain.posts.NormalPosts;
import com.leadpet.www.infrastructure.domain.reply.normal.NormalReply;
import com.leadpet.www.infrastructure.domain.users.*;
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
    private final InitAdminService initAdminService;
    private final InitPostService initPostService;
    private final InitBreedService initBreedService;
    private final InitNormalReplyService initNormalReplyService;
    private final InitLikedService initLikedService;

    @PostConstruct
    public void init() {
        initShelterService.init();
        initNormalUserService.init();
        initAdminService.init();
        initPostService.init();
        initBreedService.init();
        initNormalReplyService.init();
        initLikedService.init();
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
                                .profileImage("profileImage" + idx)
                                .shelterInfo(
                                        ShelterInfo.builder()
                                                .shelterName(name + " 보호소")
                                                .shelterAddress(city + " 헬로우 월드 주소 어디서나 123-123")
                                                .shelterPhoneNumber("01012341234")
                                                .shelterHomePage("https://helloworld.com")
                                                .shelterIntro("shelterIntro" + idx)
                                                .shelterAssessmentStatus(idx % 2 == 0 ? AssessmentStatus.COMPLETED : idx % 3 == 0 ? AssessmentStatus.DECLINED : AssessmentStatus.PENDING)
                                                .build()
                                )
                                .build());
            });
        }
    }

    @Component
    static class InitAdminService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            String name = "김관리자";
            em.persist(
                    Users.builder()
                            .userId("admin")
                            .loginMethod(LoginMethod.EMAIL)
                            .email("admin@email.com")
                            .name(name)
                            .uid("uid")
                            .password("adminpass")
                            .userType(UserType.ADMIN)
                            .build());
        }
    }

    @Component
    static class InitPostService {
        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            List<Users> allUsers = em.createQuery("select u from Users u").getResultList();
            List<String> titles = List.of("Love Dive", "Hype boy", "After Like", "우린 그렇게 사랑해서", "밤편지", "그때 그 순간 그대로", "Ink");
            List<String> content = List.of("Woo yeah It's so bad It's good 난 그 맘을 좀 봐야겠어 Narcissistic, my god I love it 서로를 비춘 밤 아름다운 까만 눈빛 더 빠져 깊이 넌 내게로 난 네게로 숨 참고 love dive",
                    "Hype boy 너만 원해 Hype boy 내가 전해 And we can go high 말해봐 yeah 느껴봐 mm mm Take him to the sky You know I hype you boy",
                    "또 모르지 내 마음이 저 날씨처럼 바뀔지 날 나조차 다 알 수 없으니 그게 뭐가 중요하니 지금 네게 완전히 푹 빠졌단 게 중요한 거지 아마 꿈만 같겠지만 분명 꿈이 아니야 달리 설명할 수 없는 이건 사랑일 거야 방금 내가 말한 감정 감히 의심하지 마 그냥 좋다는 게 아냐 What's after 'LIKE'?",
                    "밤하늘, 손을 잡으면 기분이 좋다며 옅은 웃음 띠며 나에게 말하다 너는 슬픈 노래를 불렀는데 그게 우리 둘의 주제곡 같았어 하루는 순진한 눈으로 나를 바라보며 매일 두 손 모아 하늘에 빈다고 우리 둘의 시간이 영원하길 그게 참 아팠는데 그립기도 하더라",
                    "이 밤 그날의 반딧불을 당신의 창 가까이 보낼게요 음 사랑한다는 말이에요 나 우리의 첫 입맞춤을 떠올려 그럼 언제든 눈을 감고 음 가장 먼 곳으로 가요 난 파도가 머물던 모래 위에 적힌 글씨처럼 그대가 멀리 사라져 버릴 것 같아",
                    "잘 지냈지? 조금은 어색해 요즘 좋아 보여 인사 나누며 사실 궁금한 게 너무 많았는데 반가움에 멍해졌죠 생각보다 오래 된 것 같은 우리 수다스럽던 그때가 생각나 뭐가 그렇게도 할 말이 많아서 밤을 지새우곤 했죠 그리운 목소리 그리던 얼굴 참 많이도 기다렸어 다시 만나자는 너의 한마디에 울컥 눈물이 나 결국 너였단 걸 알아",
                    "Got a tattoo said \"together thru life\" Carved in your name with my pocket knife And you wonder when you wake up Will it be alright? Oh, oh, oh, oh, oh Feels like there's something broken inside");
            for (Users user : allUsers) {
                for (int i = 0; i < 3; i++) {
                    int num = (int) (Math.random() * 7);
                    final String title = titles.get(num) + i;
                    final String contents = content.get(num) + i;
                    em.persist(
                            NormalPosts.builder()
                                    .normalPostId("NP_" + user.getUserId() + i)
                                    .title(title)
                                    .contents(contents)
                                    .user(user)
                                    .build());

                    // 보호소만 기부/입양 피드 작성
                    // TODO 이거 로직에서도 추가해야하지 않나?

                    if (user.getUserType() == UserType.SHELTER) {
                        em.persist(
                                DonationPosts.builder()
                                        .donationPostId("DP_" + user.getUserId() + i)
                                        .title(title)
                                        .contents(contents)
                                        .startDate(LocalDateTime.now().minusDays(1))
                                        .endDate(LocalDateTime.now().plusDays((int) (Math.random() * 10)))
                                        .donationMethod(DonationMethod.values()[i])
                                        .user(user)
                                        .build());
                        em.persist(
                                AdoptionPosts.builder()
                                        .adoptionPostId("AP_" + user.getUserId() + i)
                                        .title(title)
                                        .contents(contents)
                                        .age(i + 1)
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
            DummyBreed[] dummyBreeds = new DummyBreed[]{
                    new DummyBreed("가", "골든 리트리버", AnimalType.DOG),
                    new DummyBreed("가", "고든셰터", AnimalType.DOG),
                    new DummyBreed("사", "시츄", AnimalType.DOG),
                    new DummyBreed("사", "시바", AnimalType.DOG),
                    new DummyBreed("사", "스피츠", AnimalType.DOG),
                    new DummyBreed("파", "퍼그", AnimalType.DOG)
            };

            IntStream.range(0, dummyBreeds.length).forEach(idx -> {
                em.persist(dummyBreeds[idx].toBreed("breedId" + idx));
            });
        }

        /**
         * Dummy data 생성용 클래스
         */
        @lombok.AllArgsConstructor
        private static class DummyBreed {
            private String category;
            private String breedName;
            private AnimalType animalType;

            Breed toBreed(final String breedId) {
                return Breed.builder()
                        .breedId(breedId)
                        .category(category)
                        .breedName(breedName)
                        .animalType(animalType)
                        .build();
            }
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

    @Component
    static class InitLikedService {

        @PersistenceContext
        EntityManager em;

        @Transactional
        public void init() {
            IntStream.range(101, 150).forEach(idx -> {
                int likedCount = (int) (Math.random() * 4);
                IntStream.range(0, likedCount).forEach(likedIdx -> {
                    em.persist(
                            Liked.builder()
                                    .likedId("likedId" + idx + likedIdx)
                                    .postId("NP_app0" + likedIdx)
                                    .userId("uidkko" + idx)
                                    .build());
                });
            });
        }
    }

}
