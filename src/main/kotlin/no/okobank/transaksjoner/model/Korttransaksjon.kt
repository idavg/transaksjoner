package no.okobank.transaksjoner.model

import java.time.ZonedDateTime
import java.util.UUID

data class Korttransaksjon(
  val referanse: UUID,
  val kortnummer: Long,
  val korttype: String,
  val dato: ZonedDateTime,
  val organisasjonsnummer: String,
  val belop_ore: Int,
  val transaksjonsstatus: KorttransaksjonCsv.Transaksjonsstatus,
  val organisasjonsnavn: String,
)
