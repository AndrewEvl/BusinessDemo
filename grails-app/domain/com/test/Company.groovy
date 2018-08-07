package com.test

class Company {

    String name
    String email
    String street
    String zip

    static constraints = {
        name(unique: ['name'], blank: false)
        email(email: true, blank: false)
        street blank: false
        zip blank: false
    }

    String toString() {
        name
        email
        street
        zip
    }
}
