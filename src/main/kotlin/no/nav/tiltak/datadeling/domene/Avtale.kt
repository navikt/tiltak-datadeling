package no.nav.tiltak.datadeling.domene

import java.time.LocalDateTime

data class Avtale(
    val id: String,
    val avtaleNr: Int,
    val lopenummer: Int,
    val startDato: LocalDateTime,
    val organisasjon: String,
    val deltakerFnr: String
)
