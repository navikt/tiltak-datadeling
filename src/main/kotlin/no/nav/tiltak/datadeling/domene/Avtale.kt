package no.nav.tiltak.datadeling.domene

import com.fasterxml.jackson.annotation.JsonIgnore
import org.jooq.JSON
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
    val avtaleStatus: AvtaleStatus,
    val tiltakstype: Tiltakstype,
    val versjon: Int,
    val startDato: LocalDate?,
    val sluttDato: LocalDate?,
    val stillingstype: Stillingstype?,
    val stillingprosent: Int?,
    val antallDagerPerUke: Double?,
    val godkjentAvDeltaker: LocalDateTime?,
    val godkjentAvArbeidsgiver: LocalDateTime?,
    val godkjentAvVeileder: LocalDateTime?,
    val godkjentAvBeslutter: LocalDateTime?,
    val avtaleInngått: LocalDateTime?,
    val godkjentAvNavIdent: String?,
    val godkjentAvBeslutterNavIdent: String?,
    val godkjentPaVegneAv: Boolean?,
    val godkjentPaVegneAvArbeidsgiver: Boolean?,

    @JsonIgnore
    var rawJson: JSON? = null
) {
    fun deltakersStillingsprosent(): Int? {
        return when (tiltakstype) {
            Tiltakstype.MENTOR -> null
            else -> stillingprosent
        }
    }

    fun mentorsStillingsprosent(): Int? {
        return when (tiltakstype) {
            Tiltakstype.MENTOR -> stillingprosent
            else -> null
        }
    }
}
