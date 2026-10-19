package no.okobank.transaksjoner.data

import no.okobank.transaksjoner.enums.Transaksjonsstatus
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

data class KorttransaksjonCsv(
  val referanse: UUID,
  val kortnummer: Long,
  val korttype: String,
  val dato: ZonedDateTime,
  val organisasjonsnummer: String,
  val belop_ore: Double,
  val transaksjonsstatus: Transaksjonsstatus,
) {
  companion object {
    fun fraCsv(kolonner: List<String>) =
      KorttransaksjonCsv(
        referanse = UUID.fromString(kolonner[0]),
        kortnummer = kolonner[1].toLong(),
        korttype = kolonner[2],
        dato =
          ZonedDateTime
            .parse(kolonner[3])
            .withZoneSameInstant(ZoneId.of("Europe/Oslo")),
        organisasjonsnummer = kolonner[4],
        belop_ore = kolonner[5].toDouble(),
        transaksjonsstatus = Transaksjonsstatus.valueOf(kolonner[6]),
      )
  }
}
