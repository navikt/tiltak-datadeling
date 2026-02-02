package no.nav.tiltak.datadeling.graphql

import no.nav.tiltak.datadeling.db.tables.records.AvtaleRecord
import no.nav.tiltak.datadeling.domene.AvtaleStatus
import no.nav.tiltak.datadeling.domene.HendelseType
import no.nav.tiltak.datadeling.domene.Stillingstype
import no.nav.tiltak.datadeling.domene.Tiltakstype
import java.time.LocalDate
import java.time.ZonedDateTime
import java.util.*

data class AvtaleGQL(
    val avtaleId: UUID?,
    val avtaleNr: Int?,
    val deltakerFnr: String?,
    val bedriftNr: String?,
    val veilederNavIdent: String?,
    val tiltakstype: Tiltakstype?,
    val versjon: Int?,
    val avtaleStatus: AvtaleStatusGQL?,
    val avtaleInngaatt: ZonedDateTime?,
    val startDato: LocalDate?,
    val sluttDato: LocalDate?,
    val godkjentAvDeltaker: ZonedDateTime?,
    val godkjentAvArbeidsgiver: ZonedDateTime?,
    val godkjentAvVeileder: ZonedDateTime?,
    val godkjentAvBeslutter: ZonedDateTime?,
    val godkjentPaaVegneAv: Boolean?,
    val godkjentPaaVegneAvArbeidsgiver: Boolean?,
    val stillingstype: Stillingstype?,
    val godkjentAvNavIdent: String?,
    val godkjentAvBeslutterNavIdent: String?,
    val endretTidspunkt: ZonedDateTime?,
    val opprettetTidspunkt: ZonedDateTime?,
    val endringMottattTidspunkt: ZonedDateTime?,
    val deltakersStillingsprosent: Int?,
    val mentorsStillingsprosent: Int?,
    val antallDagerPerUke: Double?
)

enum class AvtaleStatusGQL {
    ANNULLERT,
    AVBRUTT,
    PAABEGYNT,
    MANGLER_GODKJENNING,
    KLAR_FOR_OPPSTART,
    GJENNOMFORES,
    AVSLUTTET;
}

enum class HendelseTypeGQL {
    OPPRETTET,
    GODKJENT_AV_ARBEIDSGIVER,
    GODKJENT_AV_VEILEDER,
    GODKJENT_AV_DELTAKER,
    SIGNERT_AV_MENTOR,
    GODKJENT_PAA_VEGNE_AV,
    GODKJENT_PAA_VEGNE_AV_DELTAKER_OG_ARBEIDSGIVER,
    GODKJENT_PAA_VEGNE_AV_ARBEIDSGIVER,
    GODKJENNINGER_OPPHEVET_AV_ARBEIDSGIVER,
    GODKJENNINGER_OPPHEVET_AV_VEILEDER,
    DELT_MED_DELTAKER,
    DELT_MED_ARBEIDSGIVER,
    DELT_MED_MENTOR,
    ENDRET,
    AVBRUTT,
    ANNULLERT,
    LAAST_OPP,
    GJENOPPRETTET,
    OPPRETTET_AV_ARBEIDSGIVER,
    NY_VEILEDER,
    AVTALE_FORDELT,
    TILSKUDDSPERIODE_AVSLATT,
    TILSKUDDSPERIODE_GODKJENT,
    AVTALE_FORKORTET,
    AVTALE_FORLENGET,
    MAAL_ENDRET,
    INKLUDERINGSTILSKUDD_ENDRET,
    OM_MENTOR_ENDRET,
    TILSKUDDSBEREGNING_ENDRET,
    KONTAKTINFORMASJON_ENDRET,
    STILLINGSBESKRIVELSE_ENDRET,
    OPPFOLGING_OG_TILRETTELEGGING_ENDRET,
    AVTALE_INNGAATT,
    REFUSJON_KLAR,
    REFUSJON_KLAR_REVARSEL,
    REFUSJON_FRIST_FORLENGET,
    REFUSJON_KORRIGERT,
    VARSLER_SETT,
    AVTALE_SLETTET,
    GODKJENT_FOR_ETTERREGISTRERING,
    FJERNET_ETTERREGISTRERING,
    STATUSENDRING,
    PATCH
}

fun map(hendelseType: HendelseType): HendelseTypeGQL =
    when (hendelseType) {
        HendelseType.AVBRUTT -> HendelseTypeGQL.AVBRUTT
        HendelseType.OPPRETTET -> HendelseTypeGQL.OPPRETTET
        HendelseType.OPPRETTET_AV_ARENA -> HendelseTypeGQL.OPPRETTET
        HendelseType.GODKJENT_AV_ARBEIDSGIVER -> HendelseTypeGQL.GODKJENT_AV_ARBEIDSGIVER
        HendelseType.GODKJENT_AV_VEILEDER -> HendelseTypeGQL.GODKJENT_AV_VEILEDER
        HendelseType.GODKJENT_AV_DELTAKER -> HendelseTypeGQL.GODKJENT_AV_DELTAKER
        HendelseType.SIGNERT_AV_MENTOR -> HendelseTypeGQL.SIGNERT_AV_MENTOR
        HendelseType.GODKJENT_PAA_VEGNE_AV -> HendelseTypeGQL.GODKJENT_PAA_VEGNE_AV
        HendelseType.GODKJENT_PAA_VEGNE_AV_DELTAKER_OG_ARBEIDSGIVER -> HendelseTypeGQL.GODKJENT_PAA_VEGNE_AV_DELTAKER_OG_ARBEIDSGIVER
        HendelseType.GODKJENT_PAA_VEGNE_AV_ARBEIDSGIVER -> HendelseTypeGQL.GODKJENT_PAA_VEGNE_AV_ARBEIDSGIVER
        HendelseType.GODKJENNINGER_OPPHEVET_AV_ARBEIDSGIVER -> HendelseTypeGQL.GODKJENNINGER_OPPHEVET_AV_ARBEIDSGIVER
        HendelseType.GODKJENNINGER_OPPHEVET_AV_VEILEDER -> HendelseTypeGQL.GODKJENNINGER_OPPHEVET_AV_VEILEDER
        HendelseType.DELT_MED_DELTAKER -> HendelseTypeGQL.DELT_MED_DELTAKER
        HendelseType.DELT_MED_ARBEIDSGIVER -> HendelseTypeGQL.DELT_MED_ARBEIDSGIVER
        HendelseType.DELT_MED_MENTOR -> HendelseTypeGQL.DELT_MED_MENTOR
        HendelseType.ENDRET -> HendelseTypeGQL.ENDRET
        HendelseType.ENDRET_AV_ARENA -> HendelseTypeGQL.ENDRET
        HendelseType.ANNULLERT -> HendelseTypeGQL.ANNULLERT
        HendelseType.LÅST_OPP -> HendelseTypeGQL.LAAST_OPP
        HendelseType.GJENOPPRETTET -> HendelseTypeGQL.GJENOPPRETTET
        HendelseType.OPPRETTET_AV_ARBEIDSGIVER -> HendelseTypeGQL.OPPRETTET_AV_ARBEIDSGIVER
        HendelseType.NY_VEILEDER -> HendelseTypeGQL.NY_VEILEDER
        HendelseType.AVTALE_FORDELT -> HendelseTypeGQL.AVTALE_FORDELT
        HendelseType.TILSKUDDSPERIODE_AVSLATT -> HendelseTypeGQL.TILSKUDDSPERIODE_AVSLATT
        HendelseType.TILSKUDDSPERIODE_GODKJENT -> HendelseTypeGQL.TILSKUDDSPERIODE_GODKJENT
        HendelseType.AVTALE_FORKORTET -> HendelseTypeGQL.AVTALE_FORKORTET
        HendelseType.AVTALE_FORKORTET_AV_ARENA -> HendelseTypeGQL.AVTALE_FORKORTET
        HendelseType.AVTALE_FORLENGET -> HendelseTypeGQL.AVTALE_FORLENGET
        HendelseType.AVTALE_FORLENGET_AV_ARENA -> HendelseTypeGQL.AVTALE_FORLENGET
        HendelseType.MÅL_ENDRET -> HendelseTypeGQL.MAAL_ENDRET
        HendelseType.INKLUDERINGSTILSKUDD_ENDRET -> HendelseTypeGQL.INKLUDERINGSTILSKUDD_ENDRET
        HendelseType.OM_MENTOR_ENDRET -> HendelseTypeGQL.OM_MENTOR_ENDRET
        HendelseType.TILSKUDDSBEREGNING_ENDRET -> HendelseTypeGQL.TILSKUDDSBEREGNING_ENDRET
        HendelseType.KONTAKTINFORMASJON_ENDRET -> HendelseTypeGQL.KONTAKTINFORMASJON_ENDRET
        HendelseType.STILLINGSBESKRIVELSE_ENDRET -> HendelseTypeGQL.STILLINGSBESKRIVELSE_ENDRET
        HendelseType.OPPFØLGING_OG_TILRETTELEGGING_ENDRET -> HendelseTypeGQL.OPPFOLGING_OG_TILRETTELEGGING_ENDRET
        HendelseType.AVTALE_INNGÅTT -> HendelseTypeGQL.AVTALE_INNGAATT
        HendelseType.REFUSJON_KLAR -> HendelseTypeGQL.REFUSJON_KLAR
        HendelseType.REFUSJON_KLAR_REVARSEL -> HendelseTypeGQL.REFUSJON_KLAR_REVARSEL
        HendelseType.REFUSJON_FRIST_FORLENGET -> HendelseTypeGQL.REFUSJON_FRIST_FORLENGET
        HendelseType.REFUSJON_KORRIGERT -> HendelseTypeGQL.REFUSJON_KORRIGERT
        HendelseType.VARSLER_SETT -> HendelseTypeGQL.VARSLER_SETT
        HendelseType.AVTALE_SLETTET -> HendelseTypeGQL.AVTALE_SLETTET
        HendelseType.GODKJENT_FOR_ETTERREGISTRERING -> HendelseTypeGQL.GODKJENT_FOR_ETTERREGISTRERING
        HendelseType.FJERNET_ETTERREGISTRERING -> HendelseTypeGQL.FJERNET_ETTERREGISTRERING
        HendelseType.STATUSENDRING -> HendelseTypeGQL.STATUSENDRING
        HendelseType.PATCH -> HendelseTypeGQL.PATCH
    }

fun map(avtaleStatus: AvtaleStatus): AvtaleStatusGQL =
    when (avtaleStatus) {
        AvtaleStatus.AVBRUTT -> AvtaleStatusGQL.AVBRUTT
        AvtaleStatus.ANNULLERT -> AvtaleStatusGQL.ANNULLERT
        AvtaleStatus.PÅBEGYNT -> AvtaleStatusGQL.PAABEGYNT
        AvtaleStatus.MANGLER_GODKJENNING -> AvtaleStatusGQL.MANGLER_GODKJENNING
        AvtaleStatus.KLAR_FOR_OPPSTART -> AvtaleStatusGQL.KLAR_FOR_OPPSTART
        AvtaleStatus.GJENNOMFØRES -> AvtaleStatusGQL.GJENNOMFORES
        AvtaleStatus.AVSLUTTET -> AvtaleStatusGQL.AVSLUTTET
    }

fun map(avtaleStatus: AvtaleStatusGQL): AvtaleStatus =
    when (avtaleStatus) {
        AvtaleStatusGQL.AVBRUTT -> AvtaleStatus.AVBRUTT
        AvtaleStatusGQL.ANNULLERT -> AvtaleStatus.ANNULLERT
        AvtaleStatusGQL.PAABEGYNT -> AvtaleStatus.PÅBEGYNT
        AvtaleStatusGQL.MANGLER_GODKJENNING -> AvtaleStatus.MANGLER_GODKJENNING
        AvtaleStatusGQL.KLAR_FOR_OPPSTART -> AvtaleStatus.KLAR_FOR_OPPSTART
        AvtaleStatusGQL.GJENNOMFORES -> AvtaleStatus.GJENNOMFØRES
        AvtaleStatusGQL.AVSLUTTET -> AvtaleStatus.AVSLUTTET
    }

fun map(avtaleRecord: AvtaleRecord): AvtaleGQL =
    AvtaleGQL(
        avtaleId = avtaleRecord.avtaleId,
        avtaleNr = avtaleRecord.avtaleNr,
        deltakerFnr = avtaleRecord.deltakerFnr,
        bedriftNr = avtaleRecord.bedriftNr,
        veilederNavIdent = avtaleRecord.veilederNavIdent,
        tiltakstype = Tiltakstype.valueOf(avtaleRecord.tiltakstype),
        versjon = avtaleRecord.versjon,
        avtaleStatus = map(AvtaleStatus.valueOf(avtaleRecord.avtaleStatus)),
        avtaleInngaatt = avtaleRecord.avtaleInngaatt?.toZonedDateTime(),
        startDato = avtaleRecord.startDato,
        sluttDato = avtaleRecord.sluttDato,
        godkjentAvDeltaker = avtaleRecord.godkjentAvDeltaker?.toZonedDateTime(),
        godkjentAvArbeidsgiver = avtaleRecord.godkjentAvArbeidsgiver?.toZonedDateTime(),
        godkjentAvVeileder = avtaleRecord.godkjentAvVeileder?.toZonedDateTime(),
        godkjentAvBeslutter = avtaleRecord.godkjentAvBeslutter?.toZonedDateTime(),
        godkjentPaaVegneAv = avtaleRecord.godkjentPaaVegneAv,
        godkjentPaaVegneAvArbeidsgiver = avtaleRecord.godkjentPaaVegneAvArbeidsgiver,
        stillingstype = avtaleRecord.stillingstype?.let { Stillingstype.valueOf(it) },
        godkjentAvNavIdent = avtaleRecord.godkjentAvNavIdent,
        godkjentAvBeslutterNavIdent = avtaleRecord.godkjentAvBeslutterNavIdent,
        endretTidspunkt = avtaleRecord.endretTidspunkt.toZonedDateTime(),
        opprettetTidspunkt = avtaleRecord.opprettetTidspunkt.toZonedDateTime(),
        endringMottattTidspunkt = avtaleRecord.endringMottattTidspunkt.toZonedDateTime(),
        deltakersStillingsprosent = avtaleRecord.deltakersStillingsprosent,
        mentorsStillingsprosent = avtaleRecord.mentorsStillingsprosent,
        antallDagerPerUke = avtaleRecord.antallDagerPerUke
    )
