package com.test


import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import groovy.json.JsonSlurper

import static org.springframework.http.HttpStatus.*

class CompanyController {

    CompanyService companyService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond companyService.list(params), model: [companyCount: companyService.count()]
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def show(Long id) {
        def company = companyService.get(id)
        if (company.getLng() == null) {
            def mapsUrl = requestYandexMapsUrl(company)
            companyService.save(mapsUrl)
        }
        [company: companyService.get(id)]
    }

    @Secured(['ROLE_USER', 'ROLE_ADMIN'])
    def mapEncoder() {

        List companyList = Company.list()
        def data = companyList
                .collect { comp ->
            return [comp.coordinates,
            ]
        }
        render(view: "mapEncoder", model: [data: data])
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
    def upload() {
        def name
        def email
        def street
        def zip
        def importCompany = 0

        try {
            request.getFile('myFile')
                    .inputStream
                    .splitEachLine(';') { fields ->
                name = fields[0]
                email = fields[1]
                street = fields[2]
                zip = fields[3]

                def company = new Company(name, email, street, zip)
                requestYandexMapsUrl(company).save()
                importCompany++
            }
            flash.message = message(code: 'default.company.add.message', args: [message(code: importCompany)])
                redirect action: "index", method: "GET"
        }

        catch (NullPointerException) {
            flash.message = message(code: 'default.exists.db.message', args: [message(code: name)])
            redirect action: "index", method: "GET"
        }
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
            requestYandexMapsUrl(company).save()
//            companyService.save(company)
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

    private static Company requestYandexMapsUrl(Company company) {

        URL url = new URL("https://geocode-maps.yandex.ru/1.x/?format=json&geocode=" + company.getStreet().replaceAll("\\s", "+"))
        HttpURLConnection connection = (HttpURLConnection) url.openConnection()
        connection.setRequestMethod("GET")

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))

//        def reader = new FileReader("D:\\projects\\demo\\jsonRespouns.json")
        JsonSlurper jsonSlurper = new JsonSlurper()
        Object result = jsonSlurper.parse(reader)

        def jsonResult = result
        def location = jsonResult.response.GeoObjectCollection.featureMember.GeoObject.Point
        String coordinate = location.pos
        String lat = coordinate.find("[\\d.]+\\s").replaceAll("\\s", "")
        String lng = coordinate.find("\\s[\\d.]+").replaceAll("\\s", "")

        company.setCoordinates(lng + ", " + lat)
        company.setLng(lng)
        company.setLat(lat)

        reader.close()
        return company
    }

}
