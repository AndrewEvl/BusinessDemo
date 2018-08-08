package com.test


class Company {

    String name
    String email
    String street
    String zip
    String lat
    String lng

    static constraints = {
        name(unique: ['name'], blank: false)
        email(blank: false)
        street (blank: false)
        zip (blank: false)
        lat (nullable: true)
        lng (nullable: true)
    }

    String toString() {
        name
        email
        street
        zip
    }
}
