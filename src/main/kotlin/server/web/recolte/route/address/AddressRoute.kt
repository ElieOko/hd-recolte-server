package server.web.recolte.route.address

import server.web.recolte.route.GlobalRoute

object CommuneScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AddressFeatures.COMMUNES}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AddressFeatures.COMMUNES}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${AddressFeatures.COMMUNES}"
}

object DistrictScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/${AddressFeatures.DISTRICTS}"
    const val PROTECTED = "${GlobalRoute.PROTECT}/${AddressFeatures.DISTRICTS}"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/${AddressFeatures.DISTRICTS}"
}

object AddressFeatures {
    const val COMMUNES = "communes"
    const val DISTRICTS = "districts"
}
