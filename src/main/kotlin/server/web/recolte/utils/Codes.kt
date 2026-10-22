package server.web.recolte.utils

object Codes {
    const val AGENT = "hda-"
    const val CLIENT = "hdc-"

    fun next(prefix: String, count: Long): String = "$prefix${count + 1}"
}
