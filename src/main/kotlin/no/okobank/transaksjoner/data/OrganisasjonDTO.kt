package no.okobank.transaksjoner.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrganisasjonDTO(
  val navn: String,
)
