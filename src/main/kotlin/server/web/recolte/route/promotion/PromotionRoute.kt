package server.web.recolte.route.promotion

import server.web.recolte.route.GlobalRoute

object PromotionScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/promotions"
    const val PROTECTED = "${GlobalRoute.PROTECT}/promotions"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/promotions"
}
