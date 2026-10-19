package no.okobank.transaksjoner.controller

import no.okobank.transaksjoner.enums.Transaksjonsstatus
import no.okobank.transaksjoner.model.Korttransaksjon
import no.okobank.transaksjoner.service.KorttransaksjonService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.UUID

@RestController
class KorttransaksjonController(
  private val korttransaksjonService: KorttransaksjonService,
) {
  @GetMapping("/api/korttransaksjoner")
  fun hentKorttransaksjoner(
    @RequestParam(value = "transaksjonsstatus", required = false)
    transaksjonsstatus: Transaksjonsstatus?,
    @RequestParam(required = false)
    fraDato: ZonedDateTime?,
    @RequestParam(required = false)
    tilDato: ZonedDateTime?,
  ): List<Korttransaksjon> = korttransaksjonService.hentTransaksjoner(transaksjonsstatus, fraDato, tilDato)

  @GetMapping("/api/{transaksjonsreferanse}")
  fun hentKorttransaksjon(
    @PathVariable transaksjonsreferanse: UUID,
  ): List<Korttransaksjon> = korttransaksjonService.hentTransaksjon(transaksjonsreferanse)

  @GetMapping("/api/oppsummering")
  fun hentOppsummering(): List<Korttransaksjon> = TODO("Provide the return value")
}
