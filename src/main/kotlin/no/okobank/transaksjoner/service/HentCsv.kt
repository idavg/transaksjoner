package no.okobank.transaksjoner.service

import no.okobank.transaksjoner.model.TransaksjonerDTO
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File

@Component
class HentCsv {
    // @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Oslo")
    @EventListener(ApplicationReadyEvent::class)
    @Scheduled(cron = "0 * * * * *", zone = "Europe/Oslo")
    fun importerCsv() {
        File("transaksjoner.csv")
            .useLines { lines ->
                lines
                    .drop(1)
                    .map { it.split(",") }
                    .mapIndexed { index, kolonner ->
                        try {
                            TransaksjonerDTO.fraCsv(kolonner)
                        } catch (e: IllegalArgumentException) {
                            throw IllegalArgumentException(
                                "Feil i CSV-rad ${index + 2}: ${e.message}",
                                e,
                            )
                        }
                    }.toList()
            }
    }
}
