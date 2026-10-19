package no.okobank.transaksjoner.client

import no.okobank.transaksjoner.model.OrganisasjonDTO
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Configuration
class WebClientConfig {
    @Bean
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()
}

@Component
class BronnoysundClient(
    builder: WebClient.Builder,
) {
    private val webclient =
        builder
            .baseUrl("http://localhost:8080")
            .build()

    fun hentOrganisasjonsNavn(organisasjonsnummer: String): OrganisasjonDTO? =
        webclient
            .get()
            .uri("/api/v1/organisasjonsnavn?orgnummer=$organisasjonsnummer")
            .retrieve()
            .bodyToMono<OrganisasjonDTO>()
            .block()
}
