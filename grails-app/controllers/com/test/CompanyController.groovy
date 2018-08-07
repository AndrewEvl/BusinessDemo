package com.test

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*
import com.xlson.groovycsv.CsvParser

class CompanyController {

    CompanyService companyService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond companyService.list(params), model:[companyCount: companyService.count()]
    }

    def show(Long id) {
        [company: companyService.get(id)]
    }

    def findByName (String name){
        def company = Company.findByName(name)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    def findByStreet (String street){
        def company = Company.findByStreet(street)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    def findByEmail (String email){
        def company = Company.findByEmail(email)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    def upload = {
//        def company = new Company()
//        new File("users.csv").splitEachLine(",") { fields ->
//            company.add(
//                    name: fields[0],
//                    email: fields[1],
//                    street: fields[2])

        println params.filecsv
        def data = CsvParser.parseCsv('filecsv')
        for (line in data) {
            def company = new Company(
                    name: "${line.name}",
                    street: "${line.street}",
                    email: "${line.email}",
                    zip: "${line.zip}"
            )
            company.save()
        }
    }

    def create() {
        respond new Company(params)
    }

    def save(Company company) {
        if (company == null) {
            notFound()
            return
        }

        try {
            companyService.save(company)
        } catch (ValidationException e) {
            respond company.errors, view:'create'
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

    def edit(Long id) {
        respond companyService.get(id)
    }

    def update(Company company) {
        if (company == null) {
            notFound()
            return
        }

        try {
            companyService.save(company)
        } catch (ValidationException e) {
            respond company.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'company.label', default: 'Company'), company.id])
                redirect company
            }
            '*'{ respond company, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        companyService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'company.label', default: 'Company'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'company.label', default: 'Company'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
