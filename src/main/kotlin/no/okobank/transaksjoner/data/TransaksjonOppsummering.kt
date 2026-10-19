package no.okobank.transaksjoner.data

import no.okobank.transaksjoner.enums.Transaksjonsstatus

data class TransaksjonOppsummering(
  val organisasjoner: List<OrganisasjonsOppsummering>,
)

data class OrganisasjonsOppsummering(
  val organisasjonsnummer: String,
  val totalBelop: Double,
  val antallPerStatus: Map<Transaksjonsstatus, Long>,
)

data class OrganisasjonsBelop(
  val organisasjonsnummer: String,
  val totalBelopOre: Long,
)
