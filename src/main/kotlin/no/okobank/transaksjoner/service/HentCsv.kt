package no.okobank.transaksjoner.service

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File

@Component
class HentCsv {
    @Scheduled(cron = "0 0 3 * * *", zone = "Europe/Oslo")
    fun importerCsv() {
        File("transaksjoner.csv").forEachLine { line ->
            val kolonner = line.split(",")
            println(kolonner)
        }
    }
}
