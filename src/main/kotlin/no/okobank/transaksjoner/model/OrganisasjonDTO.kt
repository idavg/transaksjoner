package no.okobank.transaksjoner.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrganisasjonDTO(
  val navn: String,
)
