package no.nav.tiltak.datadeling

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.tiltak.datadeling.db.tables.records.AvtaleRecord
import no.nav.tiltak.datadeling.db.tables.references.AVTALE
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.graphql.AvtaleGQL
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.impl.DSL
import org.jooq.impl.DSL.jsonGetAttributeAsText
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId

@Component
class AvtaleRepository(
    val dslContext: DSLContext
) {
    fun hentAvtale(params: Map<String, String>, minimer: List<String>): List<AvtaleGQL> {
        if (params.containsKey("avtaleId")) {
            return hentAvtale(params.get("avtaleId")!!)
        } else if (params.containsKey("avtaleNr")) {
            return hentAvtaleVedNr(params.get("avtaleNr")?.toInt()!!)
        }
        return emptyList()
    }

    fun hentAvtale(avtaleId: String): List<AvtaleGQL> = dslContext.selectFrom(AVTALE)
        .where(jsonGetAttributeAsText(AVTALE.AVTALE_DATA, "avtaleId").eq(avtaleId)).fetch().map {
            map(it)
        }

    private fun map(avtaleRecord: AvtaleRecord?): AvtaleGQL {
        return jacksonObjectMapper().readValue(avtaleRecord?.avtaleData?.data(), AvtaleGQL::class.java)
    }

    fun hentAvtaleVedNr(avtaleNr: Int): List<AvtaleGQL> = dslContext.selectFrom(AVTALE)
        .where(jsonGetAttributeAsText(AVTALE.AVTALE_DATA, "avtaleNr").eq(avtaleNr.toString())).fetch().map {
            map(it)
        }

    fun save(avtale: Avtale): Avtale? {
        return dslContext.transactionResult { it ->
//        val sisteAvtale = dslContext.selectFrom(AVTALE).where(
//            AVTALE.AVTALE_ID.eq(avtale.avtaleId),
//            AVTALE.GYLDIG_TIL_TIDSPUNKT.isNull
//        ).fetch().firstOrNull()
//        val nyGyldigFra = OffsetDateTime.now()
//        if (sisteAvtale != null) {
//            dslContext.update(AVTALE)
//                .set(AVTALE.GYLDIG_TIL_TIDSPUNKT, nyGyldigFra)
//                .where(AVTALE.AVTALE_ID.eq(avtale.avtaleId).and(AVTALE.GYLDIG_TIL_TIDSPUNKT.isNull))
//                .execute()
//        }
            DSL.using(it).insertInto(AVTALE)
                .set(
                    dslContext.newRecord(
                        AVTALE, AvtaleRecord(
                            null,
                            JSON.valueOf(
                                jacksonObjectMapper().writeValueAsString(avtale)
                            ),
                            avtale.startDato?.toOsloOffset()!!,
                            avtale.sluttDato?.toOsloOffset(),
                            OffsetDateTime.now()
                        )
                    )
                )
                .returning()
                .fetchOneInto(Avtale::class.java)
        }
    }

    fun count(): Long = 0

}

private fun Instant.toOsloOffset(): OffsetDateTime = this.atZone(ZoneId.of("Europe/Oslo")).toOffsetDateTime()
private fun LocalDate.toOsloOffset(): OffsetDateTime = this.atStartOfDay(ZoneId.of("Europe/Oslo")).toOffsetDateTime()
private fun LocalDateTime.toOsloOffset(): OffsetDateTime = this.atZone(ZoneId.of("Europe/Oslo")).toOffsetDateTime()
