package no.okobank.transaksjoner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TransaksjonerApplication

fun main(args: Array<String>) {
  runApplication<TransaksjonerApplication>(*args)
}
