package com.test

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller: 'company', action: "index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
