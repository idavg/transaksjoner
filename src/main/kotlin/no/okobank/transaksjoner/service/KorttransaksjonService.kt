package no.okobank.transaksjoner.service

import no.okobank.transaksjoner.data.Korttransaksjon
import no.okobank.transaksjoner.data.OrganisasjonsOppsummering
import no.okobank.transaksjoner.data.TransaksjonOppsummering
import no.okobank.transaksjoner.enums.Transaksjonsstatus
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

  fun hentOppsummertData(): TransaksjonOppsummering {
    val organisasjonsnumre =
      korttransaksjonRepository.hentAlleOrganisasjonsnumre()

    val organisasjoner =
      organisasjonsnumre.map { orgnr ->
        val belop =
          korttransaksjonRepository.hentTotalBelopPerOrganisasjon(orgnr)

        val antallPerStatus =
          korttransaksjonRepository.hentAntallPerStatusPerOrganisasjon(orgnr)

        OrganisasjonsOppsummering(
          organisasjonsnummer = orgnr,
          totalBelop = (belop.firstOrNull()?.totalBelopOre ?: 0L).toDouble() / 100,
          antallPerStatus = antallPerStatus,
        )
      }

    return TransaksjonOppsummering(
      organisasjoner = organisasjoner,
    )
  }
}
