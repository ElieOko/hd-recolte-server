package server.web.recolte.route.agent

import server.web.recolte.route.GlobalRoute

object AgentScope {
    const val RESOURCE = "agent"
    const val AUTH = "agent/auth"
    const val PUBLIC = "${GlobalRoute.PUBLIC}/agents"
    const val PROTECTED = "${GlobalRoute.PROTECT}/agents"
    const val PRIVATE = "${GlobalRoute.PRIVATE}/agents"
}
