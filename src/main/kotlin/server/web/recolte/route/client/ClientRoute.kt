package server.web.recolte.route.client

import server.web.recolte.route.GlobalRoute

object ClientScope {
    const val RESOURCE = "client"
    const val AUTH = "client/auth"
    const val PUBLIC = "${GlobalRoute.PUBLIC}/clients"
    const val PROTECTED = "${GlobalRoute.PROTECT}/clients"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/clients"
}
