package server.web.recolte.utils

object Codes {
    const val AGENT = "hda-"
    const val CLIENT = "HDC-"

    fun next(prefix: String, count: Long): String = "$prefix${count + 1}"
}
