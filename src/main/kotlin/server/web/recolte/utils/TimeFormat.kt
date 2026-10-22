package server.web.recolte.utils

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object TimeFormat {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'")

    fun now(): LocalDateTime = LocalDateTime.now(ZoneOffset.UTC)

    fun format(value: LocalDateTime?): String? = value?.format(formatter)
}
