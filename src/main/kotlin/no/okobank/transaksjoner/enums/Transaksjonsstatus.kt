package no.okobank.transaksjoner.enums

enum class Transaksjonsstatus(
  val value: String,
) {
  GODKJENT("GODKJENT"),
  AVVIST("AVVIST"),
  VENTENDE("VENTENDE"),
}
