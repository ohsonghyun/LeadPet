package com.leadpet.www

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

import javax.persistence.EntityManager

/**
 * QuerydslTestUtil
 */
@TestConfiguration
class TestConfig {
    @Bean
    JPAQueryFactory queryFactory(EntityManager em) {
        return new JPAQueryFactory(em)
    }
}
