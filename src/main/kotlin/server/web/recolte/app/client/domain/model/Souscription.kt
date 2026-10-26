package server.web.recolte.app.client.domain.model

import org.springframework.http.HttpStatus
import server.web.recolte.exception.ApiException

data class TrancheMensuelle(
    val numero: Int,
    val montant: Long,
)

data class Souscription(
    val montantSouscription: Long,
    val devise: String,
    val nombreTranches: Int,
    val tranche: String,
    val tranches: List<TrancheMensuelle>,
)

data class ClientRegistration(
    val address: AddressClient,
    val souscription: Souscription,
)

object Tranches {
    private val labels = linkedMapOf(
        1 to "une tranche",
        2 to "deux tranche",
        3 to "trois tranche",
        4 to "quatre tranche",
    )

    fun of(montant: Long, count: Int, devise: String = "FC"): Souscription {
        val nombre = count.coerceIn(1, 4)
        return Souscription(
            montantSouscription = montant,
            devise = normalizeStored(devise),
            nombreTranches = nombre,
            tranche = labels.getValue(nombre),
            tranches = split(montant, nombre),
        )
    }

    fun require(montant: Long?, nombre: Int?, label: String?, devise: String?): Souscription {
        if (montant == null) {
            throw invalid(
                mapOf(
                    "montant_souscription" to listOf("The montant_souscription field is required."),
                ),
            )
        }
        if (montant <= 0L) {
            throw invalid("Le montant de souscription doit être supérieur à 0")
        }
        val count = resolve(nombre, label)
        return of(montant, count, requireDevise(devise))
    }

    private fun requireDevise(value: String?): String {
        if (value.isNullOrBlank()) {
            throw invalid(mapOf("devise" to listOf("The devise field is required.")))
        }
        return when (value.trim().uppercase()) {
            "FC" -> "FC"
            "$" -> "$"
            else -> throw invalid("La devise doit être FC ou $")
        }
    }

    private fun normalizeStored(value: String): String =
        if (value.trim() == "$") "$" else "FC"

    private fun resolve(nombre: Int?, label: String?): Int {
        val fromNombre = nombre?.takeIf { it in labels }
        val fromLabel = label?.let(::parseLabel)
        if (nombre != null && fromNombre == null) {
            throw invalid(choiceMessage("nombre_tranches"))
        }
        if (label != null && label.isNotBlank() && fromLabel == null) {
            throw invalid(choiceMessage("tranche"))
        }
        if (fromNombre != null && fromLabel != null && fromNombre != fromLabel) {
            throw invalid("La tranche ne correspond pas au nombre de tranches")
        }
        return fromNombre ?: fromLabel ?: throw invalid(
            mapOf("nombre_tranches" to listOf("The nombre_tranches field is required.")),
        )
    }

    private fun parseLabel(value: String): Int? {
        val text = value.trim().lowercase().replace(Regex("\\s+"), " ")
        if (text.isEmpty()) return null
        labels.entries.firstOrNull { it.value == text }?.let { return it.key }
        val singular = text.removeSuffix("s")
        labels.entries.firstOrNull { it.value == singular }?.let { return it.key }
        return text.toIntOrNull()?.takeIf { it in labels }
    }

    private fun split(montant: Long, count: Int): List<TrancheMensuelle> {
        val base = montant / count
        val remainder = (montant % count).toInt()
        return (1..count).map { numero ->
            TrancheMensuelle(
                numero = numero,
                montant = base + if (numero <= remainder) 1 else 0,
            )
        }
    }

    private fun choiceMessage(field: String): Map<String, List<String>> = mapOf(
        field to listOf("Choisissez une tranche, deux tranche, trois tranche ou quatre tranche."),
    )

    private fun invalid(message: Any): ApiException =
        ApiException(HttpStatus.BAD_REQUEST, mapOf("message" to message))
}
