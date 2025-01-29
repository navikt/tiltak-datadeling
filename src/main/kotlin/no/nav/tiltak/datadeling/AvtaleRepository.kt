package no.nav.tiltak.datadeling

import no.nav.tiltak.datadeling.db.tables.records.AvtaleRecord
import no.nav.tiltak.datadeling.db.tables.references.AVTALE
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.graphql.AvtaleGQL
import no.nav.tiltak.datadeling.graphql.map
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Field
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*

@Component
class AvtaleRepository(
    val dslContext: DSLContext
) {
    final val tilGqlFelt = AVTALE.fields().map {
        it.name to it.name
            .replace("å", "aa")
            .replace("ø", "oe")
            .replace("æ", "ae")
            .replace(Regex("_(\\w)"), { matchResult: MatchResult -> matchResult.groupValues.last().uppercase() })
    }.toMap()
    val tilTabellFelt = tilGqlFelt.map { it.value to it.key }.toMap()

    fun hentAvtale(params: Map<String, String>, minimer: List<String>): List<AvtaleGQL> {
        val kolonner = minimer
            .map { tilTabellFelt.getOrDefault(it, it) }
            .map { AVTALE.field(it) }
            .filterNotNull()
        if (params.containsKey("avtaleId")) {
            return hentAvtaleQuery(AVTALE.AVTALE_ID.eq(UUID.fromString(params.get("avtaleId")!!)), kolonner)
        } else if (params.containsKey("avtaleNr")) {
            return hentAvtaleQuery(AVTALE.AVTALE_NR.eq(params.get("avtaleNr")?.toInt()!!), kolonner)
        } else if (params.containsKey("tiltakstype")) {
            return hentAvtaleQuery(AVTALE.TILTAKSTYPE.eq(params.get("tiltakstype")), kolonner)
        } else if (params.containsKey("avtaleStatus")) {
            return hentAvtaleQuery(AVTALE.AVTALE_STATUS.eq(params.get("avtaleStatus")), kolonner)
        } else if (params.containsKey("bedriftNr")) {
            return hentAvtaleQuery(AVTALE.BEDRIFT_NR.eq(params.get("bedriftNr")), kolonner)
        } else if (params.containsKey("deltakerFnr")) {
            return hentAvtaleQuery(AVTALE.DELTAKER_FNR.eq(params.get("deltakerFnr")), kolonner)
        }
        return emptyList()
    }

    fun hentAvtaleQuery(condition: Condition, kolonner: List<Field<*>>): List<AvtaleGQL> =
        dslContext.selectFrom(AVTALE)
            .where(condition)
            .map { map(it) }

    fun save(avtale: Avtale): AvtaleRecord? = dslContext.transactionResult { it ->
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
            OffsetDateTime.now()
        )
        // Og sett inn ny
        return@transactionResult it.dsl().insertInto(AVTALE)
            .set(nyAvtaleRecord).onConflict(AVTALE.AVTALE_ID)
            .doUpdate().set(nyAvtaleRecord)
            .where(AVTALE.ENDRET_TIDSPUNKT.le(avtale.sistEndret.toOsloOffset()))
            .returning()
            .fetchOneInto(AvtaleRecord::class.java)
    }

    fun count() = dslContext.fetchCount(AVTALE);
}

private val osloSone = ZoneId.of("Europe/Oslo")
fun Instant.toOsloOffset(): OffsetDateTime = this.atZone(osloSone).toOffsetDateTime()
fun LocalDateTime.toOsloOffset(): OffsetDateTime = this.atZone(osloSone).toOffsetDateTime()
