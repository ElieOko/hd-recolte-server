package server.web.recolte.exception

import org.springframework.http.HttpStatus

class ApiException(
    val status: HttpStatus,
    val body: Map<String, Any?>,
) : RuntimeException(body["message"]?.toString() ?: status.reasonPhrase)
