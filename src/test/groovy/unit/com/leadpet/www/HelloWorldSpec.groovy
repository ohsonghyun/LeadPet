package com.leadpet.www

import spock.lang.Specification
import spock.lang.Unroll

class HelloWorldSpec extends Specification{

    @Unroll
    def "helloworld: #testCase"() {
        when:
        def first = i
        def second = j

        then:
        result == first + second

        where:
        testCase | i | j | result
        "1+1" | 1 | 1 | 2
        "1+2" | 1 | 2 | 3
        "2+2" | 2 | 2 | 4
    }

}
