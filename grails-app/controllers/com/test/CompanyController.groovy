package com.test

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CompanyController {

    CompanyService companyService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond companyService.list(params), model: [companyCount: companyService.count()]
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def show(Long id) {
        [company: companyService.get(id)]
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def findByName(String name) {
        def company = Company.findByName(name)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def findByStreet(String street) {
        def company = Company.findByStreet(street)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def findByEmail(String email) {
        def company = Company.findByEmail(email)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def upload (String filecsv){
        def file = request.getFile('fivecsv')
        def path = filecsv
        def street
        def email
        def zip
        def name
        def importCompany = 0
//        new File("C:\\Users\\ami\\Desktop\\1.csv").splitEachLine(',') { fields ->
        try {
            new File("C:\\Users\\ami\\Desktop\\1.csv").splitEachLine(',') { fields ->
                name = fields[0].trim()
                email = fields[1].trim()
                street = fields[2].trim()
                zip = fields[3].trim()
                def company = new Company()
                company.setName(name)
                company.setEmail(email)
                company.setStreet(street)
                company.setZip(zip)
                companyService.save(company)
                importCompany++

                println "---" + company.getName() + " save"
                if (company.hasErrors() || company.save(flush: true) == null) {
                    log.error("___not save ${company.name}")
                }
                log.debug("___Importing domainObject  ${company.toString()}")
            }
        }catch(ignored){
            redirect(action: "index")
            render(view: 'index', model: [importCompany: importCompany])
        }
    }

    def map =  {
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def create() {
        respond new Company(params)
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def save(Company company) {
        if (company == null) {
            notFound()
            return
        }

        try {
            companyService.save(company)
        } catch (ValidationException e) {
            respond company.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'company.label', default: 'Company'), company.id])
                redirect company
            }
            '*' { respond company, [status: CREATED] }
        }
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def edit(Long id) {
        respond companyService.get(id)
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def update(Company company) {
        if (company == null) {
            notFound()
            return
        }

        try {
            companyService.save(company)
        } catch (ValidationException e) {
            respond company.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'company.label', default: 'Company'), company.id])
                redirect company
            }
            '*' { respond company, [status: OK] }
        }
    }

    @Secured(['ROLE_USER','ROLE_ADMIN'])
    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        companyService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

}
