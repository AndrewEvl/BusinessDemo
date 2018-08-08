package com.test

class Company {

    String name
    String email
    String street
    String zip

    static constraints = {
        name(unique: ['name'], blank: false)
        email(blank: false)
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
