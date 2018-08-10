package com.test


class BootStrap {

    def init = { servletContext ->
        def company = new Company(
                name: "Company",
                street: "Партизанский проспект 19, Минск",
                email: "tets@mail.com",
                zip: "220037",
                lat: "53.8840516",
                lng: "27.5892717"

        )
        company.save()
        println("___@@@___" +
                "\nCompany created")
        if (company.hasErrors()) {
            println("Company has broken")
        }

        def adminRole = Role.findOrSaveWhere(authority: 'ROLE_ADMIN')
        def userRole = Role.findOrSaveWhere(authority: 'ROLE_USER')

        def admin = User.findOrSaveWhere(username: 'root', password:  'root')
        def user = User.findOrSaveWhere(username: 'user', password: 'user')

        if(!admin.authorities.contains(adminRole)){
            UserRole.create(admin,adminRole,true)
        }

        if(!user.authorities.contains(adminRole)){
            UserRole.create(user,userRole,true)
        }
    }
    def destroy = {
    }
}
