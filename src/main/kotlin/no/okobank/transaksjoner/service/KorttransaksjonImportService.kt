package no.okobank.transaksjoner.service

import no.okobank.transaksjoner.client.BronnoysundClient
import no.okobank.transaksjoner.model.Korttransaksjon
import no.okobank.transaksjoner.model.KorttransaksjonCsv
import no.okobank.transaksjoner.repository.KorttransaksjonRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File

@Component
class KorttransaksjonImportService(
  private val korttransaksjonRepository: KorttransaksjonRepository,
  private val bronnoysundClient: BronnoysundClient,
) {
  // @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Oslo")
  @EventListener(ApplicationReadyEvent::class)
  @Scheduled(cron = "0 * * * * *", zone = "Europe/Oslo")
  fun dagligKorttransaksjonJob() {
    lesCsvOgLagre()
  }

  fun lesCsvOgLagre() {
    val dagensTransaksjoner = importerCsv()
    val berikedeTransaksjoner = berikTransaksjoner(dagensTransaksjoner)
    berikedeTransaksjoner.forEach { berikede ->
      korttransaksjonRepository.opprettTransaksjon(berikede)
    }
  }

  fun berikTransaksjoner(dagensTransaksjoner: List<KorttransaksjonCsv>): List<Korttransaksjon> {
    val organisasjonsnavn =
      dagensTransaksjoner
        .map { it.organisasjonsnummer }
        .distinct()
        .associateWith { bronnoysundClient.hentOrganisasjonsNavn(it) }
    println(organisasjonsnavn)
    return dagensTransaksjoner.map { transaksjon ->
      Korttransaksjon(
        referanse = transaksjon.referanse,
        kortnummer = transaksjon.kortnummer,
        korttype = transaksjon.korttype,
        dato = transaksjon.dato,
        organisasjonsnummer = transaksjon.organisasjonsnummer,
        belop_ore = transaksjon.belop_ore,
        transaksjonsstatus = transaksjon.transaksjonsstatus,
        organisasjonsnavn = organisasjonsnavn[transaksjon.organisasjonsnummer]?.navn ?: "Ukjent orgnr",
      )
    }
  }

  fun importerCsv(): List<KorttransaksjonCsv> =
    File("transaksjoner.csv")
      .useLines { lines ->
        lines
          .drop(1)
          .map { it.split(",") }
          .mapIndexed { index, kolonner ->
            try {
              KorttransaksjonCsv.fraCsv(kolonner)
            } catch (e: IllegalArgumentException) {
              throw IllegalArgumentException(
                "Feil i CSV-rad ${index + 2}: ${e.message}",
                e,
              )
            }
          }.toList()
      }
}
