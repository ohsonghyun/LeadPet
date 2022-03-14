package com.leadpet.www.infrastructure.domain.users

import spock.lang.Specification
import spock.lang.Unroll

/**
 * UsersSpec
 */
class UsersSpec extends Specification {

    @Unroll("#testCase")
    def "유저아이디 생성: uid + loginMethod"() {
        given:
        Users user = Users.builder()
                .loginMethod(loginMethod)
                .uid(uid)
                .name(name)
                .userType(userType)
                .build()

        when:
        user.createUserId()

        then:
        user.getUserId() == result

        where:
        testCase | loginMethod        | uid   | name    | userType        | result
        "KAKAO"  | LoginMethod.KAKAO  | "uid" | "kakao" | UserType.NORMAL | "uidkko"
        "GOOGLE" | LoginMethod.GOOGLE | "uid" | "goole" | UserType.NORMAL | "uidggl"
        "APPLE"  | LoginMethod.APPLE  | "uid" | "apple" | UserType.NORMAL | "uidapp"
        "EMAIL"  | LoginMethod.EMAIL  | "uid" | "email" | UserType.NORMAL | "uideml"
    }
}
