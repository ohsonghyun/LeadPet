package com.leadpet.www.infrastructure.domain.users

import spock.lang.Specification

/**
 * ShelterInfoSpec
 */
class ShelterInfoSpec extends Specification {

    def "보호소 관련 정보 수정"() {
        given:
        def shelterInfo = new ShelterInfo('shelterName', 'shelterAddress', 'shelterPhoneNumber', 'shelterManager', 'shelterHomePage', 'shelterInfo', 'shelterAccount', AssessmentStatus.COMPLETED)

        when:
        shelterInfo.update(newShelterName, newShelterAddress, newShelterPhoneNumber, newShelterManager, newShelterHomePage, newShelterIntro, newShelterAccount)

        then:
        shelterInfo.getShelterName() == newShelterName
        shelterInfo.getShelterAddress() == newShelterAddress
        shelterInfo.getShelterPhoneNumber() == newShelterPhoneNumber
        shelterInfo.getShelterManager() == newShelterManager
        shelterInfo.getShelterHomePage() == newShelterHomePage
        shelterInfo.getShelterIntro() == newShelterIntro
        shelterInfo.getShelterAccount() == newShelterAccount

        where:
        newShelterName   | newShelterAddress   | newShelterPhoneNumber   | newShelterManager   | newShelterHomePage   | newShelterIntro   | newShelterAccount
        'newShelterName' | 'newShelterAddress' | 'newShelterPhoneNumber' | 'newShelterManager' | 'newShelterHomePage' | 'newShelterIntro' | 'newShelterAccount'
    }
}
