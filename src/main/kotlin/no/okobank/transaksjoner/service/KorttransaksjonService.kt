package no.okobank.transaksjoner.service

import no.okobank.transaksjoner.enums.Transaksjonsstatus
import no.okobank.transaksjoner.model.Korttransaksjon
import no.okobank.transaksjoner.repository.KorttransaksjonRepository
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.UUID

@Service
class KorttransaksjonService(
  private val korttransaksjonRepository: KorttransaksjonRepository,
) {
  fun hentTransaksjoner(
    status: Transaksjonsstatus?,
    fraDato: ZonedDateTime?,
    tilDato: ZonedDateTime?,
  ): List<Korttransaksjon> = korttransaksjonRepository.hentKorttransaksjoner(status, fraDato, tilDato)

  fun hentTransaksjon(referanse: UUID): List<Korttransaksjon> =
    korttransaksjonRepository.hentKorttransaksjonPaaReferanse(referanse)
}
