package blockchain

class UrlMappings {

    static mappings = {
        delete "/$controller/$id(.$format)?"(action:"delete")
        get "/$controller(.$format)?"(action:"index")
        get "/$controller/$id(.$format)?"(action:"show")
        post "/$controller(.$format)?"(action:"save")
        put "/$controller/$id(.$format)?"(action:"update")
        patch "/$controller/$id(.$format)?"(action:"patch")



        "/transaction/new"(controller: 'transaction', action: 'create')
        "/transaction/chain"(controller: 'transaction', action: 'chain')
        "/transaction/mine"(controller: 'transaction', action: 'mine')


        "/node/register"(controller: 'transaction', action: 'registerNode')

        "/node/resolve"(controller: 'transaction', action: 'consensus')

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
