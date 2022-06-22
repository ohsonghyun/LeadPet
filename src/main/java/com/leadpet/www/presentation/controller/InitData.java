package com.leadpet.www.presentation.controller;

import com.leadpet.www.infrastructure.domain.users.LoginMethod;
import com.leadpet.www.infrastructure.domain.users.UserType;
import com.leadpet.www.infrastructure.domain.users.Users;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private final InitShelterService initShelterService;

    @PostConstruct
    public void init() {
        initShelterService.init();
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
                                .build());
            });
        }
    }
}
