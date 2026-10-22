package server.web.recolte.route.trash

import server.web.recolte.route.GlobalRoute

object TrashScope {
    const val RESOURCE = "trash"
    const val SHOW = "trash/{id}"
    const val CLEAN = "trash/clean/{id}"
    const val PUBLIC = "${GlobalRoute.PUBLIC}/trashes"
    const val PROTECTED = "${GlobalRoute.PROTECT}/trashes"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/trashes"
}

object StateTrashScope {
    const val PUBLIC = "${GlobalRoute.PUBLIC}/state-trashes"
    const val PROTECTED = "${GlobalRoute.PROTECT}/state-trashes"
}
