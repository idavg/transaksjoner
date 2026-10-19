package no.okobank.transaksjoner.data

import no.okobank.transaksjoner.enums.Transaksjonsstatus
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.time.ZonedDateTime
import java.util.UUID

data class Korttransaksjon(
  val referanse: UUID,
  val kortnummer: Long,
  val korttype: String,
  val dato: ZonedDateTime,
  val organisasjonsnummer: String,
  val belop_kr: Double,
  val transaksjonsstatus: Transaksjonsstatus,
  val organisasjonsnavn: String,
) {
  companion object {
    fun fraResultSet(rs: ResultSet): Korttransaksjon =
      Korttransaksjon(
        referanse = rs.getObject("referanse", UUID::class.java),
        kortnummer = rs.getLong("kortnummer"),
        korttype = rs.getString("korttype"),
        dato =
          rs
            .getObject("dato", OffsetDateTime::class.java)
            .toZonedDateTime(),
        organisasjonsnummer = rs.getString("organisasjonsnummer"),
        belop_kr = rs.getDouble("belop_ore") / 100,
        transaksjonsstatus =
          Transaksjonsstatus.valueOf(
            rs.getString("transaksjonsstatus"),
          ),
        organisasjonsnavn = rs.getString("organisasjonsnavn"),
      )
  }
}
