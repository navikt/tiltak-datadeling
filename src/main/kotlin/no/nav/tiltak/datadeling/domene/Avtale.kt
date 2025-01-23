package no.nav.tiltak.datadeling.domene

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class Avtale(
    val avtaleId: UUID,
    val avtaleNr: Int,
    val opprettetTidspunkt: LocalDateTime,
    val sistEndret: Instant,
    val bedriftNr: String,
    val deltakerFnr: String,
    val veilederNavIdent: String?,
    //val hendelseType: HendelseType,
    val avtaleStatus: AvtaleStatus,
    val tiltakstype: Tiltakstype,
    val versjon: Int,
    val startDato: LocalDate?,
    val sluttDato: LocalDate?,
    val stillingstype: Stillingstype?,
    val godkjentAvDeltaker: LocalDateTime?,
    val godkjentAvArbeidsgiver: LocalDateTime?,
    val godkjentAvVeileder: LocalDateTime?,
    val godkjentAvBeslutter: LocalDateTime?,
    val avtaleInngått: LocalDateTime?,
    val godkjentAvNavIdent: String?,
    val godkjentAvBeslutterNavIdent: String?,
    val godkjentPaVegneAv: Boolean?,
    val godkjentPaVegneAvArbeidsgiver: Boolean?
)
