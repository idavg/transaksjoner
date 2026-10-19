package no.okobank.transaksjoner.client

import no.okobank.transaksjoner.data.OrganisasjonDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Configuration
class WebClientConfig {
  @Bean
  fun webClientBuilder(): WebClient.Builder = WebClient.builder()
}

@Component
class BronnoysundClient(
  builder: WebClient.Builder,
  @Value("\${bronnoysund.baseUrl}") bronnoysundBaseUrl: String,
) {
  private val webclient =
    builder
      .baseUrl(bronnoysundBaseUrl)
      .build()

  fun hentOrganisasjonsNavn(organisasjonsnummer: String): OrganisasjonDTO? =
    webclient
      .get()
      .uri("/enhetsregisteret/api/enheter/$organisasjonsnummer")
      .retrieve()
      .bodyToMono<OrganisasjonDTO>()
      .onErrorResume { Mono.empty() }
      .block()
}
