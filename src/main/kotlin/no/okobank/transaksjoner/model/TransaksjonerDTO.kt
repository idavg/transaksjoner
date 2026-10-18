package no.okobank.transaksjoner.model

import java.time.ZonedDateTime
import java.util.UUID

data class TransaksjonerDTO(
    val referanse: UUID,
    val kortnummer: Long,
    val korttype: String, // TODO: Burde det være enum?
    val dato: ZonedDateTime,
    val organisasjonsnummer: String,
    val belop_ore: Int,
    val stransaksjonsstatus: Transaksjonsstatus,
) {
    enum class Transaksjonsstatus(
        val value: String,
    ) {
        GODKJENT("GODKJENT"),
        AVVIST("AVVIST"),
        VENTENDE("VENTENDE"),
    }
}
