# ØkoBank – Korttransaksjoner API

REST API for korttransaksjoner i ØkoBank AS. Applikasjonen leser inn transaksjoner fra en CSV-fil, lagrer dem i PostgreSQL og beriker dem med organisasjonsinformasjon fra Brønnøysundregistrene.

API-et gir tilgang til transaksjoner, filtrering og aggregert statistikk per organisasjon.

## Teknologi

- Kotlin
- Spring Boot
- Java 21
- PostgreSQL
- Spring JDBC (`NamedParameterJdbcTemplate`)
- WebClient for kommunikasjon med Brønnøysundregistrene
- OpenAPI og Swagger UI for API-dokumentasjon

## Forutsetninger

Før du starter, må du ha følgende installert:

- Java 21
- Git
- Gradle Wrapper, som følger med prosjektet

## Kom i gang

### 1. Klon prosjektet

```bash
git clone https://github.com/idavg/transaksjoner.git
cd transaksjoner
```

### 2. Start PostgreSQL

Applikasjonen bruker PostgreSQL som database. Sørg for at PostgreSQL er installert og kjører lokalt.

Opprett databasen `korttransaksjonerdb`, og kontroller at brukernavn, passord, port og databasenavn samsvarer med konfigurasjonen i `application.yaml`.


Eksempel:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/korttransaksjonerdb
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

Sett miljøvariablene før du starter applikasjonen.

