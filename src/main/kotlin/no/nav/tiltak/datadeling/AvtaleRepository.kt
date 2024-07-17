package no.nav.tiltak.datadeling

import no.nav.tiltak.datadeling.db.tables.records.AvtaleRecord
import no.nav.tiltak.datadeling.db.tables.references.AVTALE
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.graphql.AvtaleGQL
import no.nav.tiltak.datadeling.graphql.map
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

val MAKS_TID = OffsetDateTime.of(9999, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)

@Component
class AvtaleRepository(
    val dslContext: DSLContext
) {
    fun hentAvtale(params: Map<String, String>, minimer: List<String>): List<AvtaleGQL> {
        if (params.containsKey("avtaleId")) {
            return hentAvtale(params.get("avtaleId")!!)
        } else if (params.containsKey("avtaleNr")) {
            return hentAvtaleVedNr(params.get("avtaleNr")?.toInt()!!)
        } else if (params.containsKey("tiltakstype")) {
            return hentAvtaleVedTiltakstype(params.get("tiltakstype")!!)
        } else if (params.containsKey("avtaleStatus")) {
            return hentAvtaleVedStatus(params.get("avtaleStatus")!!)
        }
        return emptyList()
    }

    fun hentAvtale(avtaleId: String): List<AvtaleGQL> = dslContext.selectFrom(AVTALE)
        .where(AVTALE.AVTALE_ID.eq(UUID.fromString(avtaleId)))
        .fetch().map {
            map(it)
        }

    fun hentAvtaleVedNr(avtaleNr: Int): List<AvtaleGQL> = dslContext.selectFrom(AVTALE)
        .where(AVTALE.AVTALE_NR.eq(avtaleNr))
        .fetch().map {
            map(it)
        }

    fun hentAvtaleVedTiltakstype(tiltakstype: String): List<AvtaleGQL> = dslContext.selectFrom(AVTALE)
        .where(AVTALE.TILTAKSTYPE.eq(tiltakstype))
        .fetch().map {
            map(it)
        }

    fun hentAvtaleVedStatus(status: String): List<AvtaleGQL> = dslContext.selectFrom(AVTALE)
        .where(AVTALE.AVTALE_STATUS.eq(status))
        .fetch().map {
            map(it)
        }

    fun save(avtale: Avtale): AvtaleRecord? = dslContext.transactionResult { it ->
        // Sett gyldig-til for forrige versjon (hvis vi har en tidligere versjon)
//        it.dsl().deleteFrom(AVTALE)
//            .where(AVTALE.AVTALE_ID.eq(avtale.avtaleId))
//            .and(AVTALE.GYLDIG_TIL.eq(MAKS_TID))
//            .execute()

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
            avtale.sistEndret.toOsloOffset(),
            MAKS_TID,
            OffsetDateTime.now()
        )
        // Og sett inn ny
        return@transactionResult it.dsl().insertInto(AVTALE)
            .set(nyAvtaleRecord).onConflict(AVTALE.AVTALE_ID)
            .doUpdate().set(nyAvtaleRecord)
            .returning()
            .fetchOneInto(AvtaleRecord::class.java)
    }

    fun count() = dslContext.fetchCount(AVTALE);
}

private val osloSone = ZoneId.of("Europe/Oslo")
private fun Instant.toOsloOffset(): OffsetDateTime = this.atZone(osloSone).toOffsetDateTime()
private fun LocalDateTime.toOsloOffset(): OffsetDateTime = this.atZone(osloSone).toOffsetDateTime()
