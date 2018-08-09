package com.test

import grails.testing.gorm.DomainUnitTest
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class CompanySpec extends Specification implements DomainUnitTest<Company> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
