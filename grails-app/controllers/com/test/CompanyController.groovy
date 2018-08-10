package com.test

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import groovy.json.JsonSlurper

import static org.springframework.http.HttpStatus.*

class CompanyController {

    CompanyService companyService

    static final googleMapKey = "AIzaSyAkb-iWBBb8gtoQsWnZmQJeqt5wPUQQaf8"
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond companyService.list(params), model: [companyCount: companyService.count()]
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def show(Long id) {
        [company: companyService.get(id)]
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def mapEncoder(String address) {

    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def findByName(String name) {
        def company = Company.findByName(name)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def findByStreet(String street) {
        def company = Company.findByStreet(street)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def findByEmail(String email) {
        def company = Company.findByEmail(email)
        if (!company.getName().isEmpty()) {
            render(view: 'show', model: [company: companyService.get(company.getId())])
        } else redirect(action: "index")
    }


    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def upload(String filecsv) {
        def urltest = "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyDjL4WbUKAMlKNG8ub6jwpxkuSYpk3mFX0"
//        def file = request.getFile('fivecsv')
        def path = filecsv

        def street
        def email
        def zip
        def name
        def importCompany = 0
//        new File("C:\\Users\\ami\\Desktop\\1.csv").splitEachLine(',') { fields ->
        try {
            def file1 = new File("C:\\Users\\ami\\Desktop\\1.csv")
            file1.splitEachLine(';') { fields ->
                name = fields[0].trim()
                email = fields[1].trim()
                street = fields[2].trim()
                zip = fields[3].trim()
                def company = new Company(name, email, street, zip)
                def googleUrl = "https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + street.replaceAll("\\s", "+")
                println googleUrl
                def companyLatLng = requestGoogleMapsUrl(company).save()
                importCompany++
                println "---" + companyLatLng.getName() + " save"
                flash.message = message(code: 'default.company.add.message', args: [message(code: importCompany)])
                redirect(action: "index")
            }
        } catch (ignored) {
            flash.message = message(code: 'default.not.save.message', args: [message(code: name)])
            redirect action: "index", method: "POST"
        }
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def map() {
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def create() {
        respond new Company(params)
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
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

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def edit(Long id) {
        respond companyService.get(id)
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
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

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
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

    private static Company requestGoogleMapsUrl(Company company) {

        URL url = new URL("https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + company.getStreet().replaceAll("\\s", "+"))
        HttpURLConnection connection = (HttpURLConnection) url.openConnection()
        connection.setRequestMethod("GET")

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))

//        def reader = new FileReader("D:\\projects\\demo\\jsonRespouns.json")
        JsonSlurper jsonSlurper = new JsonSlurper()
        Object result = jsonSlurper.parse(reader)

        def jsonResult =  result
        def location = jsonResult.response.GeoObjectCollection.featureMember.GeoObject.Point
        String coordinate = location.pos
        String lat = coordinate.find("[\\d.]+\\s").replaceAll("\\s","")
        String lng =coordinate.find("\\s[\\d.]+").replaceAll("\\s","")
        println coordinate
        company.setLng(lng)
        company.setLat(lat)

        reader.close()
        return company
    }

}
