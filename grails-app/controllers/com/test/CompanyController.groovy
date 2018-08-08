package com.test

import grails.validation.ValidationException
import groovy.sql.Sql

import static org.springframework.http.HttpStatus.*
import com.xlson.groovycsv.CsvParser
import com.opencsv.CSVReader

class CompanyController {

    CompanyService companyService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond companyService.list(params), model: [companyCount: companyService.count()]
    }

    def show(Long id) {
        [company: companyService.get(id)]
    }

    def findByName(String name) {
        def company = Company.findByName(name)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    def findByStreet(String street) {
        def company = Company.findByStreet(street)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    def findByEmail(String email) {
        def company = Company.findByEmail(email)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    def upload (String filecsv){
//        request.getFile('filecsv')
//                .inputStream
//                .splitEachLine(',') { fields ->
//            def company = new Company(name: fields[0].trim(),
//                    email: fields[1].trim(),
//                    street: fields[2].trim(),
//                    zip: fields[3].trim())
//
//            if (company.hasErrors() || company.save(flush: true) == null) {
//                log.error("Could not import domainObject  ${company.errors}")
//            }
//
//            log.debug("Importing domainObject  ${company.toString()}")
//        }

//        def sql = Sql.newInstance("jdbc:mysql://localhost:3306/demo", "root", "root", "com.mysql.cj.jdbc.Driver")
//        def company = sql.dataSet("company")
//        new File("C:\\Users\\ami\\Desktop\\1.csv").splitEachLine(",") { fields ->
//            company.add(name: fields[0],
//                    email: fields[1],
//                    street: fields[2],
//                    zip: fields[3]
//            )
        def street
        def email
        def zip
        def name
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
                if (company.hasErrors() || company.save(flush: true) == null) {
                    log.error("___not save ${company.name}")
                }
                log.debug("___Importing domainObject  ${company.toString()}")
            }
        }catch(ignored){
            redirect(action: "index")
        }
//        println params.filecsv
//        def data = CsvParser.parseCsv("C:\\Users\\ami\\Desktop\\1.csv")
//        for (line in data) {
//            def company = new Company(
//                    name: "${line.name}",
//                    street: "${line.street}",
//                    email: "${line.email}",
//                    zip: "${line.zip}"
//            )
//            company.save()
//        }

//        List<String[]> company = new CSVReader(new InputStreamReader(getClass()
//                .classLoader
//                .getResourceAsStream('filecsv')))
//                .readAll()
//        println company
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
