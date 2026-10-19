package no.okobank.transaksjoner.repository

import no.okobank.transaksjoner.enums.Transaksjonsstatus
import no.okobank.transaksjoner.model.Korttransaksjon
import org.slf4j.LoggerFactory
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.Types
import java.time.ZonedDateTime
import java.util.UUID

@Repository
class KorttransaksjonRepository(
  private val jdbcTemplate: JdbcTemplate,
) {
  private val logger = LoggerFactory.getLogger(KorttransaksjonRepository::class.java)

  val namedParameterJdbcTemplate = NamedParameterJdbcTemplate(jdbcTemplate)

  fun opprettTransaksjon(korttransaksjon: Korttransaksjon): Boolean {
    val sql =
      """
      INSERT INTO korttransaksjon (
      referanse,
      kortnummer,
      korttype, 
      dato,
      organisasjonsnummer,
      belop_ore,
      transaksjonsstatus,
      organisasjonsnavn
      ) VALUES (
      :referanse,
      :kortnummer,
      :korttype,
      :dato,
      :organisasjonsnummer,
      :belop_ore,
      :transaksjonsstatus,
      :organisasjonsnavn
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
        .addValue("organisasjonsnavn", korttransaksjon.organisasjonsnavn)

    return try {
      logger.debug("Lagrer korttransaskjon for referanse: ${korttransaksjon.referanse}")
      namedParameterJdbcTemplate.update(sql, parametre) > 0
    } catch (e: Exception) {
      logger.error("Feil ved lagring av korttransaksjon")
      throw e
    }
  }

  fun hentKorttransaksjoner(
    status: Transaksjonsstatus?,
    fraDato: ZonedDateTime?,
    tilDato: ZonedDateTime?,
  ): List<Korttransaksjon> {
    val where = mutableListOf<String>()
    val params = mutableMapOf<String, Any?>()

    if (status != null) {
      where += "transaksjonsstatus = :status"
      params["status"] = status.name
    }

    if (fraDato != null) {
      where += "dato >= :fraDato"
      params["fraDato"] = fraDato.toOffsetDateTime()
    }

    if (tilDato != null) {
      where += "dato <= :tilDato"
      params["tilDato"] = tilDato.toOffsetDateTime()
    }
    val sql =
      buildString {
        append("SELECT * FROM korttransaksjon")
        if (where.isNotEmpty()) {
          append(" WHERE ")
          append(where.joinToString(" AND "))
        }
      }

    return namedParameterJdbcTemplate.query(sql, params) { rs, _ ->
      Korttransaksjon.fraResultSet(rs)
    }
  }

  fun hentKorttransaksjonPaaReferanse(referanse: UUID): List<Korttransaksjon> {
    val sql =
      """
      SELECT * FROM korttransaksjon
      WHERE referanse = :referanse
      """.trimIndent()

    return namedParameterJdbcTemplate.query(
      sql,
      mapOf("referanse" to referanse),
    ) { rs, _ ->
      Korttransaksjon.fraResultSet(rs)
    }
  }
}
