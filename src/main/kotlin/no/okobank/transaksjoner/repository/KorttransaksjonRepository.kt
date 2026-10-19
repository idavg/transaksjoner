package no.okobank.transaksjoner.repository

import no.okobank.transaksjoner.model.KorttransaksjonCsv
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Types

@Repository
class KorttransaksjonRepository(
    private val jdbcTemplate: JdbcTemplate,
) {
    private val logger = LoggerFactory.getLogger(KorttransaksjonRepository::class.java)

    fun opprettTransaksjon(korttransaksjon: KorttransaksjonCsv) {
        val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)
        val sql =
            """
            INSERT INTO korttransaksjon (
            referanse,
            kortnummer,
            korttype, 
            dato,
            organisasjonsnummer,
            belop_ore,
            transaksjonsstatus
            ) VALUES (
            :referanse,
            :kortnummer,
            :korttype,
            :dato,
            :organisasjonsnummer,
            :belop_ore,
            :transaksjonsstatus
            ) ON CONFLICT (referanse) DO NOTHING
            """.trimIndent()

        val parametre =
            MapSqlParameterSource()
                .addValue("referanse", korttransaksjon.referanse)
                .addValue("kortnummer", korttransaksjon.kortnummer)
                .addValue("korttype", korttransaksjon.korttype)
                .addValue(
                    "dato",
                    korttransaksjon.dato.toOffsetDateTime(),
                    Types.TIMESTAMP_WITH_TIMEZONE,
                ).addValue("organisasjonsnummer", korttransaksjon.organisasjonsnummer)
                .addValue("belop_ore", korttransaksjon.belop_ore)
                .addValue("transaksjonsstatus", korttransaksjon.transaksjonsstatus.name)

        try {
            logger.debug("Lagrer korttransaskjon for referanse: ${korttransaksjon.referanse}")
            namedParameterJdbcTemplate.update(sql, parametre)
            logger.info("Korttransaksjon lagret i database")
        } catch (e: Exception) {
            logger.error("Feil ved lagring av korttransaksjon")
            throw e
        }
    }
}
