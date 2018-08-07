package com.test

class BootStrap {

    def init = { servletContext ->
        def company = new Company(
                name: "Company",
                street: "Партизанский проспект 19, Минск",
                email: "tets@mail.com",
                zip: "220037"

        )
        company.save()
        println("___@@@___" +
                "\nCompany created")
        if (company.hasErrors()) {
            println("Company has broken")
        }
    }
    def destroy = {
    }
}
