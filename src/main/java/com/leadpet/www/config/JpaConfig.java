package com.leadpet.www.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    /**
     * Querydsl JpaQueryFactory 반환
     *
     * @param entityManager
     * @return {@code JpaQueryFactory}
     */
    @Bean(name = "queryFactory")
    public JPAQueryFactory queryFactory(final EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
}
