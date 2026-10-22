package server.web.recolte.route.user

import server.web.recolte.route.GlobalRoute

object UserScope {
    const val PUBLIC_AUTH = "${GlobalRoute.PUBLIC}/${GlobalRoute.AUTH}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/users"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/users"
}
