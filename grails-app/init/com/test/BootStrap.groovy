package com.test

class BootStrap {

    def init = { servletContext ->
        def company = new Company(
                name: "Company",
                street: "Партизанский проспект 21, Минск",
                email: "tets@mail.com",
                zip: "220037",


        )
        def companyTwo = new Company(
                name: "Company2",
                street: "Partyzanski praspiekt 19 Minsk Belarus",
                email: "Test@mail.com",
                zip: "220033",


        )
        def companyTree = new Company(
                name: "Company3",
                street: "Partyzanski praspiek 15 Minsk Belarus",
                email: "tets2@mail.com",
                zip: "220037",
        )
        company.save()
        companyTwo.save()
        companyTree.save()


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
