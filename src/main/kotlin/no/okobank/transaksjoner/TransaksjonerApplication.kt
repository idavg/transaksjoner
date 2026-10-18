package no.okobank.transaksjoner

import no.okobank.transaksjoner.service.hentCSV
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransaksjonerApplication

fun main(args: Array<String>) {
    runApplication<TransaksjonerApplication>(*args)
    println("Leser csv:")
    hentCSV()
    println("Ferdig")
}
