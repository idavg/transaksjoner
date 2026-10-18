package no.okobank.transaksjoner.model

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

data class TransaksjonerDTO(
    val referanse: UUID,
    val kortnummer: Long,
    val korttype: String, // TODO: Burde det være enum?
    val dato: ZonedDateTime,
    val organisasjonsnummer: String,
    val belop_ore: Int,
    val transaksjonsstatus: Transaksjonsstatus,
) {
    enum class Transaksjonsstatus(
        val value: String,
    ) {
        GODKJENT("GODKJENT"),
        AVVIST("AVVIST"),
        VENTENDE("VENTENDE"),
    }

    companion object {
        fun fraCsv(kolonner: List<String>) =
            TransaksjonerDTO(
                referanse = UUID.randomUUID(),
                kortnummer = kolonner[1].toLong(),
                korttype = kolonner[2],
                dato =
                    ZonedDateTime
                        .parse(kolonner[3])
                        .withZoneSameInstant(ZoneId.of("Europe/Oslo")),
                organisasjonsnummer = kolonner[4],
                belop_ore = kolonner[5].toInt(),
                transaksjonsstatus = Transaksjonsstatus.valueOf(kolonner[6]),
            )
    }
}
