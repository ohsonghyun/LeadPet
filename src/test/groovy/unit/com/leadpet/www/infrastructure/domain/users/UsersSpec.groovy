package com.leadpet.www.infrastructure.domain.users

import com.leadpet.www.TestConfig
import com.leadpet.www.infrastructure.db.users.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.transaction.annotation.Transactional
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * UsersSpec
 */
@DataJpaTest
@Import(TestConfig)
@Transactional
class UsersSpec extends Specification {

    @PersistenceContext
    EntityManager em

    @Autowired
    UsersRepository usersRepository

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

    @Unroll
    def "보호소 정보 수정"() {
        given:
        usersRepository.save(
                Users.builder()
                        .loginMethod(LoginMethod.KAKAO)
                        .uid(uid)
                        .name('name')
                        .userId(userId)
                        .userType(UserType.SHELTER)
                        .profileImage('profileImage')
                        .shelterName('shelterName')
                        .shelterAddress('shelterAddress')
                        .shelterManager('shelterManager')
                        .shelterHomePage('shelterHomePage')
                        .shelterPhoneNumber('01012341234')
                        .shelterIntro('shelterIntro')
                        .shelterAccount('shelterAccount')
                        .build())

        def newShelterInfo = ShelterInfo.builder()
                .shelterName(newShelterName)
                .shelterAddress(newShelterAddress)
                .shelterPhoneNumber(newShelterPhoneNumber)
                .shelterIntro(newShelterIntro)
                .shelterAccount(newShelterAccount)
                .shelterManager(newShelterManager)
                .shelterHomePage(newShelterHomePage)
                .build()

        when:
        def shelter = usersRepository.findShelterByUserId(userId)
        shelter.updateShelter(newShelterInfo)

        em.flush()
        em.clear()

        then:
        def updatedShelter = usersRepository.findShelterByUserId(userId)

        updatedShelter.getShelterName() == newShelterName
        updatedShelter.getShelterAddress() == newShelterAddress
        updatedShelter.getShelterPhoneNumber() == newShelterPhoneNumber
        updatedShelter.getShelterIntro() == newShelterIntro
        updatedShelter.getShelterAccount() == newShelterAccount
        updatedShelter.getShelterManager() == newShelterManager
        updatedShelter.getShelterHomePage() == newShelterHomePage

        where:
        uid   | userId   | newShelterName   | newShelterAddress   | newShelterPhoneNumber | newShelterIntro   | newShelterAccount   | newShelterManager   | newShelterHomePage
        'uid' | 'userId' | 'newShelterName' | 'newShelterAddress' | '01056785678'         | 'newShelterIntro' | 'newShelterAccount' | 'newShelterManager' | 'newShelterHomePage'
    }

    def "일반유저 정보 수정"() {
        given:
        def user = usersRepository.save(
                Users.builder()
                        .userId(userId)
                        .loginMethod(LoginMethod.KAKAO)
                        .uid('uid')
                        .name('old name')
                        .intro('old intro')
                        .address('old address')
                        .profileImage('old profileImage')
                        .userType(UserType.NORMAL)
                        .build())

        def userInfo = UserInfo.builder()
                .name(name)
                .intro(intro)
                .address(address)
                .profileImage(profileImage)
                .build()
        when:
        user.updateNormalUser(userInfo)

        em.flush()
        em.clear()

        then:
        // 더티 체킹
        def updatedUser = usersRepository.findById(userId).orElseThrow()
        updatedUser.getName() == name
        updatedUser.getIntro() == intro
        updatedUser.getAddress() == address
        updatedUser.getProfileImage() == profileImage

        where:
        userId   | name   | intro   | address   | profileImage
        'userId' | 'name' | 'intro' | 'address' | 'profileImage'
    }
}
