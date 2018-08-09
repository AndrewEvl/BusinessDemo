package com.test

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class CompanyServiceSpec extends Specification {

    CompanyService companyService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        new Company("test","test@mail.by","Test Street", "123456").save(flush: true, failOnError: true)
        new Company("test1","test1@mail.by","Test Street One", "654321").save(flush: true, failOnError: true)
        Company company = new Company("test2","test2@mail.by","Test Street two", "123654").save(flush: true, failOnError: true)
        new Company("test3","test3@mail.by","Test Street tree", "321654").save(flush: true, failOnError: true)
        new Company("test4","test4@mail.by","Test Street four", "654987").save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        company.id
    }

    void "test get"() {
        setupData()

        expect:
        companyService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:""
        List<Company> companyList = companyService.list(max: 2, offset: 2)

        then:
        companyList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        companyService.count() == 5
    }

    void "test delete"() {
        Long companyId = setupData()

        expect:
        companyService.count() == 5

        when:
        companyService.delete(companyId)
        sessionFactory.currentSession.flush()

        then:
        companyService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Company company = new Company()
        companyService.save(company)

        then:
        company.id != null
    }
}
