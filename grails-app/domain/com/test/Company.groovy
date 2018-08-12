package com.test


class Company {

    String name
    String email
    String street
    String zip
    String lat
    String lng
    String coordinates

    static constraints = {
        name(unique: ['name'], blank: false)
        email(blank: false)
        street (blank: false)
        zip (blank: false)
        lat (nullable: true)
        lng (nullable: true)
        coordinates(nullable: true)
    }

    String toString() {
        name
        email
        street
        zip
    }

    Company() {
    }

    Company(String name, String email, String street, String zip, String lat, String lng) {
        this.name = name
        this.email = email
        this.street = street
        this.zip = zip
        this.lat = lat
        this.lng = lng
    }

    Company(String name, String email, String street, String zip) {
        this.name = name
        this.email = email
        this.street = street
        this.zip = zip
    }
}
