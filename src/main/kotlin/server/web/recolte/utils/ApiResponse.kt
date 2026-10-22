package server.web.recolte.utils

data class ApiResponse<T>(
    val data: T,
)

data class ApiResponseWithMessage<T>(
    val message: String,
    val data: T,
)
