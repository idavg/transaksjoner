package no.okobank.transaksjoner.repository

import no.okobank.transaksjoner.data.Korttransaksjon
import no.okobank.transaksjoner.data.OrganisasjonsBelop
import no.okobank.transaksjoner.enums.Transaksjonsstatus
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
  jdbcTemplate: JdbcTemplate,
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
        .addValue("belop_ore", korttransaksjon.belop_kr)
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
    status: Transaksjonsstatus? = null,
    fraDato: ZonedDateTime? = null,
    tilDato: ZonedDateTime? = null,
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

  fun hentTotalBelopPerOrganisasjon(organisasjonsnummer: String): List<OrganisasjonsBelop> {
    val sql =
      """
      SELECT
          organisasjonsnummer,
          SUM(belop_ore) AS total_belop_ore
      FROM korttransaksjon
      WHERE organisasjonsnummer = :organisasjonsnummer
      GROUP BY organisasjonsnummer
      """.trimIndent()

    return namedParameterJdbcTemplate.query(
      sql,
      mapOf("organisasjonsnummer" to organisasjonsnummer),
    ) { rs, _ ->
      OrganisasjonsBelop(
        organisasjonsnummer = rs.getString("organisasjonsnummer"),
        totalBelopOre = rs.getLong("total_belop_ore"),
      )
    }
  }

  fun hentAntallPerStatusPerOrganisasjon(organisasjonsnummer: String): Map<Transaksjonsstatus, Long> {
    val sql =
      """
      SELECT
          transaksjonsstatus,
          COUNT(*) AS antall
      FROM korttransaksjon
      WHERE organisasjonsnummer = :organisasjonsnummer
      GROUP BY transaksjonsstatus
      """.trimIndent()

    val resultat =
      namedParameterJdbcTemplate
        .query(
          sql,
          mapOf("organisasjonsnummer" to organisasjonsnummer),
        ) { rs, _ ->
          Transaksjonsstatus.valueOf(
            rs.getString("transaksjonsstatus"),
          ) to rs.getLong("antall")
        }.toMap()

    return Transaksjonsstatus.entries.associateWith { status ->
      resultat[status] ?: 0L
    }
  }

  fun hentAlleOrganisasjonsnumre(): List<String> {
    val sql =
      """
      SELECT DISTINCT organisasjonsnummer
      FROM korttransaksjon
      ORDER BY organisasjonsnummer
      """.trimIndent()

    return namedParameterJdbcTemplate.query(
      sql,
      emptyMap<String, Any>(),
    ) { rs, _ ->
      rs.getString("organisasjonsnummer")
    }
  }
}
