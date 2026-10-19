package no.okobank.transaksjoner.service
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import no.okobank.transaksjoner.data.Korttransaksjon
import no.okobank.transaksjoner.enums.Transaksjonsstatus
import no.okobank.transaksjoner.repository.KorttransaksjonRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID

@SpringBootTest
class KorttransaksjonServiceTest {
  private val repository = mockk<KorttransaksjonRepository>()
  private val service = KorttransaksjonService(repository)

  private val referanse = UUID.randomUUID()

  private val transaksjon =
    Korttransaksjon(
      referanse = referanse,
      kortnummer = 1234567890123456L,
      korttype = "Visa",
      dato =
        ZonedDateTime.of(
          2026,
          1,
          15,
          12,
          0,
          0,
          0,
          ZoneOffset.UTC,
        ),
      organisasjonsnummer = "123456789",
      belop_kr = 12550.00,
      transaksjonsstatus = Transaksjonsstatus.GODKJENT,
      organisasjonsnavn = "Testbedrift AS",
    )

  @Test
  fun `hentTransaksjoner returnerer transaksjoner`() {
    every {
      repository.hentKorttransaksjoner(null, null, null)
    } returns listOf(transaksjon)

    val resultat = service.hentTransaksjoner(null, null, null)

    assertEquals(1, resultat.size)
    assertEquals(referanse, resultat.first().referanse)
    assertEquals("Testbedrift AS", resultat.first().organisasjonsnavn)

    verify(exactly = 1) {
      repository.hentKorttransaksjoner(null, null, null)
    }
  }

  @Test
  fun `hentTransaksjon returnerer transaksjon med riktig referanse`() {
    every {
      repository.hentKorttransaksjonPaaReferanse(referanse)
    } returns listOf(transaksjon)

    val resultat = service.hentTransaksjon(referanse)

    assertEquals(1, resultat.size)
    assertEquals(referanse, resultat.first().referanse)
  }

  @Test
  fun `hentTransaksjoner returnerer tom liste når ingen transaksjoner finnes`() {
    every {
      repository.hentKorttransaksjoner(null, null, null)
    } returns emptyList()

    val resultat = service.hentTransaksjoner(null, null, null)

    assertEquals(emptyList<Korttransaksjon>(), resultat)
  }
}
