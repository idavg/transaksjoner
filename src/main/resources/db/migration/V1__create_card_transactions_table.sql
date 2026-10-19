-- ============================
-- Opprettet av: Ida Viddal Gaarder
-- Opprettet: 18/10/26
-- Beskrivelse: Oppretter tabell for korttransaksjoner
-- ============================

CREATE TABLE korttransaksjon
(
    referanse               UUID                        PRIMARY KEY,
    kortnummer              varchar                     NOT NULL,
    korttype                varchar                     NOT NULL,
    dato                    timestamp with time zone    NOT NULL,
    organisasjonsnummer     varchar                     NOT NULL,
    belop_ore               int                         NOT NULL,
    transaksjonsstatus      varchar                     NOT NULL,
    organisasjonsNavn       varchar                     NOT NULL
);