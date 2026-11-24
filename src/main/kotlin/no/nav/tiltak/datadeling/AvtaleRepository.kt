package no.nav.tiltak.datadeling

import no.nav.tiltak.datadeling.db.tables.records.AvtaleRecord
import no.nav.tiltak.datadeling.db.tables.references.AVTALE
import no.nav.tiltak.datadeling.domene.Avtale
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

@Component
class AvtaleRepository(
    val dslContext: DSLContext
) {
    fun upsertHvisNyereEllerSamtidig(avtale: Avtale): AvtaleRecord? = dslContext.transactionResult { it ->
        val nyAvtaleRecord = AvtaleRecord(
            null,
            avtale.avtaleId,
            avtale.avtaleNr,
            avtale.deltakerFnr,
            avtale.bedriftNr,
            avtale.veilederNavIdent,
            avtale.tiltakstype.name,
            avtale.versjon,
            avtale.avtaleStatus.name,
            avtale.avtaleInngått?.toOsloOffset(),
            avtale.startDato,
            avtale.sluttDato,
            avtale.godkjentAvDeltaker?.toOsloOffset(),
            avtale.godkjentAvArbeidsgiver?.toOsloOffset(),
            avtale.godkjentAvVeileder?.toOsloOffset(),
            avtale.godkjentAvBeslutter?.toOsloOffset(),
            avtale.godkjentPaVegneAv,
            avtale.godkjentPaVegneAvArbeidsgiver,
            avtale.stillingstype?.name,
            avtale.godkjentAvNavIdent,
            avtale.godkjentAvBeslutterNavIdent,
            avtale.opprettetTidspunkt.toOsloOffset(),
            avtale.sistEndret.toOsloOffset(),
            OffsetDateTime.now(),
            antallDagerPerUke = avtale.antallDagerPerUke,
            stillingprosent = avtale.deltakersStillingprosent(),
            opphav = avtale.opphav?.name,
            rawJson = avtale.rawJson,
        )
        // Og sett inn ny
        return@transactionResult it.dsl().insertInto(AVTALE)
            .set(nyAvtaleRecord).onConflict(AVTALE.AVTALE_ID)
            .doUpdate().set(nyAvtaleRecord)
            .where(AVTALE.ENDRET_TIDSPUNKT.le(avtale.sistEndret.toOsloOffset()))
            .returning()
            .fetchOneInto(AvtaleRecord::class.java)
    }

    fun count() = dslContext.fetchCount(AVTALE)
}

private val osloSone = ZoneId.of("Europe/Oslo")

/**
 * JOOQ vil ikke ha instant, så vi konverterer til OffsetDateTime
 */
fun Instant.toOsloOffset(): OffsetDateTime = this.atZone(osloSone).toOffsetDateTime()
/**
 * JOOQ vil ikke ha instant, så vi konverterer til OffsetDateTime
 */
fun LocalDateTime.toOsloOffset(): OffsetDateTime = this.atZone(osloSone).toOffsetDateTime()
