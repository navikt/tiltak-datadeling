package no.nav.tiltak.datadeling.graphql

import no.nav.tiltak.datadeling.db.tables.records.AvtaleRecord
import no.nav.tiltak.datadeling.domene.AvtaleStatus
import no.nav.tiltak.datadeling.domene.HendelseType
import no.nav.tiltak.datadeling.domene.Inkluderingstilskuddsutgift
import no.nav.tiltak.datadeling.domene.InkluderingstilskuddsutgiftType
import no.nav.tiltak.datadeling.domene.InnholdType
import no.nav.tiltak.datadeling.domene.Maal
import no.nav.tiltak.datadeling.domene.MaalKategori
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
    val gyldigFraTidspunkt: ZonedDateTime?,
    val gyldigTilTidspunkt: ZonedDateTime?,
    val registrertTidspunkt: ZonedDateTime?
)

enum class InnholdTypeGQL {
    INNGAA,
    LAASE_OPP,
    FORLENGE,
    FORKORTE,
    ENDRE_MAAL,
    ENDRE_INKLUDERINGSTILSKUDD,
    ENDRE_OM_MENTOR,
    ENDRE_TILSKUDDSBEREGNING,
    ENDRE_STILLING,
    ENDRE_KONTAKTINFO,
    ENDRE_OPPFOLGING_OG_TILRETTELEGGING,
    ANNULLERE
}

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
    STATUSENDRING
}

enum class InkluderingstilskuddsutgiftTypeGQL {
    TILRETTELEGGINGSBEHOV,
    TILTAKSPLASS,
    UTSTYR,
    PROGRAMVARE,
    ARBEIDSHJELPEMIDLER,
    OPPLAERING,
    FORSIKRING_LISENS_SERTIFISERING
}

data class InkluderingstilskuddsutgiftGQL(
    val id: UUID,
    val belop: Int,
    val type: InkluderingstilskuddsutgiftTypeGQL
)

enum class MaalKategoriGQL {
    FAA_JOBB_I_BEDRIFTEN,
    ARBEIDSERFARING,
    UTPROVING,
    SPRAAKOPPLAERING,
    OPPNAA_FAGBREV_KOMPETANSEBEVIS,
    ANNET
}

data class MaalGQL(
    val id: UUID,
    val kategori: MaalKategoriGQL,
    val beskrivelse: String
)

fun map(maal: Maal): MaalGQL = MaalGQL(
    maal.id,
    map(maal.kategori),
    maal.beskrivelse,
)

fun map(maalKategori: MaalKategori): MaalKategoriGQL =
    when (maalKategori) {
        MaalKategori.UTPRØVING -> MaalKategoriGQL.UTPROVING
        MaalKategori.FÅ_JOBB_I_BEDRIFTEN -> MaalKategoriGQL.FAA_JOBB_I_BEDRIFTEN
        MaalKategori.ARBEIDSERFARING -> MaalKategoriGQL.ARBEIDSERFARING
        MaalKategori.SPRÅKOPPLÆRING -> MaalKategoriGQL.SPRAAKOPPLAERING
        MaalKategori.OPPNÅ_FAGBREV_KOMPETANSEBEVIS -> MaalKategoriGQL.OPPNAA_FAGBREV_KOMPETANSEBEVIS
        MaalKategori.ANNET -> MaalKategoriGQL.ANNET
    }

fun map(inkluderingstilskuddsutgift: Inkluderingstilskuddsutgift) = InkluderingstilskuddsutgiftGQL(
    id = inkluderingstilskuddsutgift.id,
    belop = inkluderingstilskuddsutgift.beløp,
    type = map(inkluderingstilskuddsutgift.type)
)

fun map(inkluderingstilskuddsutgiftType: InkluderingstilskuddsutgiftType) =
    when (inkluderingstilskuddsutgiftType) {
        InkluderingstilskuddsutgiftType.OPPLÆRING -> InkluderingstilskuddsutgiftTypeGQL.OPPLAERING
        InkluderingstilskuddsutgiftType.TILRETTELEGGINGSBEHOV -> InkluderingstilskuddsutgiftTypeGQL.TILRETTELEGGINGSBEHOV
        InkluderingstilskuddsutgiftType.TILTAKSPLASS -> InkluderingstilskuddsutgiftTypeGQL.TILTAKSPLASS
        InkluderingstilskuddsutgiftType.UTSTYR -> InkluderingstilskuddsutgiftTypeGQL.UTSTYR
        InkluderingstilskuddsutgiftType.PROGRAMVARE -> InkluderingstilskuddsutgiftTypeGQL.PROGRAMVARE
        InkluderingstilskuddsutgiftType.ARBEIDSHJELPEMIDLER -> InkluderingstilskuddsutgiftTypeGQL.ARBEIDSHJELPEMIDLER
        InkluderingstilskuddsutgiftType.FORSIKRING_LISENS_SERTIFISERING -> InkluderingstilskuddsutgiftTypeGQL.FORSIKRING_LISENS_SERTIFISERING
    }

fun map(hendelseType: HendelseType): HendelseTypeGQL =
    when (hendelseType) {
        HendelseType.AVBRUTT -> HendelseTypeGQL.AVBRUTT
        HendelseType.OPPRETTET -> HendelseTypeGQL.OPPRETTET
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
        HendelseType.ANNULLERT -> HendelseTypeGQL.ANNULLERT
        HendelseType.LÅST_OPP -> HendelseTypeGQL.LAAST_OPP
        HendelseType.GJENOPPRETTET -> HendelseTypeGQL.GJENOPPRETTET
        HendelseType.OPPRETTET_AV_ARBEIDSGIVER -> HendelseTypeGQL.OPPRETTET_AV_ARBEIDSGIVER
        HendelseType.NY_VEILEDER -> HendelseTypeGQL.NY_VEILEDER
        HendelseType.AVTALE_FORDELT -> HendelseTypeGQL.AVTALE_FORDELT
        HendelseType.TILSKUDDSPERIODE_AVSLATT -> HendelseTypeGQL.TILSKUDDSPERIODE_AVSLATT
        HendelseType.TILSKUDDSPERIODE_GODKJENT -> HendelseTypeGQL.TILSKUDDSPERIODE_GODKJENT
        HendelseType.AVTALE_FORKORTET -> HendelseTypeGQL.AVTALE_FORKORTET
        HendelseType.AVTALE_FORLENGET -> HendelseTypeGQL.AVTALE_FORLENGET
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
    }

fun map(innholdType: InnholdType): InnholdTypeGQL =
    when (innholdType) {
        InnholdType.INNGÅ -> InnholdTypeGQL.INNGAA
        InnholdType.LÅSE_OPP -> InnholdTypeGQL.LAASE_OPP
        InnholdType.FORLENGE -> InnholdTypeGQL.FORLENGE
        InnholdType.FORKORTE -> InnholdTypeGQL.FORKORTE
        InnholdType.ENDRE_MÅL -> InnholdTypeGQL.ENDRE_MAAL
        InnholdType.ENDRE_INKLUDERINGSTILSKUDD -> InnholdTypeGQL.ENDRE_INKLUDERINGSTILSKUDD
        InnholdType.ENDRE_OM_MENTOR -> InnholdTypeGQL.ENDRE_OM_MENTOR
        InnholdType.ENDRE_TILSKUDDSBEREGNING -> InnholdTypeGQL.ENDRE_TILSKUDDSBEREGNING
        InnholdType.ENDRE_STILLING -> InnholdTypeGQL.ENDRE_STILLING
        InnholdType.ENDRE_KONTAKTINFO -> InnholdTypeGQL.ENDRE_KONTAKTINFO
        InnholdType.ENDRE_OPPFØLGING_OG_TILRETTELEGGING -> InnholdTypeGQL.ENDRE_OPPFOLGING_OG_TILRETTELEGGING
        InnholdType.ANNULLERE -> InnholdTypeGQL.ANNULLERE
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
        avtaleInngaatt = avtaleRecord.avtaleInngått?.toZonedDateTime(),
        startDato = avtaleRecord.startDato,
        sluttDato = avtaleRecord.sluttDato,
        godkjentAvDeltaker = avtaleRecord.godkjentAvDeltaker?.toZonedDateTime(),
        godkjentAvArbeidsgiver = avtaleRecord.godkjentAvArbeidsgiver?.toZonedDateTime(),
        godkjentAvVeileder = avtaleRecord.godkjentAvVeileder?.toZonedDateTime(),
        godkjentAvBeslutter = avtaleRecord.godkjentAvBeslutter?.toZonedDateTime(),
        godkjentPaaVegneAv = avtaleRecord.godkjentPåVegneAv,
        godkjentPaaVegneAvArbeidsgiver = avtaleRecord.godkjentPåVegneAvArbeidsgiver,
        stillingstype = avtaleRecord.stillingstype?.let { Stillingstype.valueOf(it) },
        godkjentAvNavIdent = avtaleRecord.godkjentAvNavIdent,
        godkjentAvBeslutterNavIdent = avtaleRecord.godkjentAvBeslutterNavIdent,
        gyldigFraTidspunkt = avtaleRecord.gyldigFra.toZonedDateTime(),
        gyldigTilTidspunkt = avtaleRecord.gyldigTil?.toZonedDateTime(),
        registrertTidspunkt = avtaleRecord.registrertTidspunkt.toZonedDateTime()
    )

//fun map(avtale: Avtale) = AvtaleGQL(
//    avtaleId = avtale.avtaleId,
//    avtaleNr = avtale.avtaleNr,
//    opprettetTidspunkt = avtale.opprettetTidspunkt.atZone(ZoneId.systemDefault()),
//    sistEndret = avtale.sistEndret.atZone(ZoneId.systemDefault()),
//    bedriftNr = avtale.bedriftNr,
//    deltakerFnr = avtale.deltakerFnr,
//    //mentorFnr = avtale.mentorFnr,
//    veilederNavIdent = avtale.veilederNavIdent,
//    enhetGeografisk = avtale.enhetGeografisk,
//    //enhetsnavnGeografisk = avtale.enhetsnavnGeografisk,
//    enhetOppfolging = avtale.enhetOppfolging,
//    //enhetsnavnOppfolging = avtale.enhetsnavnOppfolging,
//    //hendelseType = map(avtale.hendelseType),
//    avtaleStatus = map(no.nav.tiltak.datadeling.domene.AvtaleStatus.valueOf(avtale.avtaleStatus.name)),
//    tiltakstype = Tiltakstype.valueOf(avtale.tiltakstype.name),
//    annullertTidspunkt = avtale.annullertTidspunkt?.atZone(ZoneId.systemDefault()),
//    opprettetAvArbeidsgiver = avtale.opprettetAvArbeidsgiver,
//    godkjentForEtterregistrering = avtale.godkjentForEtterregistrering,
////    kvalifiseringsgruppe = avtale.kvalifiseringsgruppe,
////    formidlingsgruppe = avtale.formidlingsgruppe,
////    tilskuddPeriode = avtale.tilskuddPeriode,
//    feilregistrert = avtale.feilregistrert,
////    versjon = avtale.versjon,
////    deltakerFornavn = avtale.deltakerFornavn,
////    deltakerEtternavn = avtale.deltakerEtternavn,
////    deltakerTlf = avtale.deltakerTlf,
////    bedriftNavn = avtale.bedriftNavn,
////    arbeidsgiverFornavn = avtale.arbeidsgiverFornavn,
////    arbeidsgiverEtternavn = avtale.arbeidsgiverEtternavn,
////    arbeidsgiverTlf = avtale.arbeidsgiverTlf,
////    veilederFornavn = avtale.veilederFornavn,
////    veilederEtternavn = avtale.veilederEtternavn,
////    veilederTlf = avtale.veilederTlf,
////    oppfolging = avtale.oppfolging,
////    tilrettelegging = avtale.tilrettelegging,
//    sluttDato = avtale.sluttDato,
//    startDato = avtale.startDato,
////    stillingprosent = avtale.stillingprosent,
////    journalpostId = avtale.journalpostId,
//    godkjentAvDeltaker = avtale.godkjentAvDeltaker,
//    godkjentAvArbeidsgiver = avtale.godkjentAvArbeidsgiver,
//    godkjentAvVeileder = avtale.godkjentAvVeileder,
////    godkjentAvBeslutter = avtale.godkjentAvBeslutter?.toLocalDateTime(),
//    //avtaleInngaatt = avtale.av,
////    enhetKostnadssted = avtale.enhetKostnadssted,
////    godkjentPaVegneGrunn = avtale.godkjentPaVegneGrunn,
////    godkjentPaVegneAv = avtale.godkjentPaVegneAv,
////    godkjentPaVegneAvArbeidsgiverGrunn = avtale.godkjentPaVegneAvArbeidsgiverGrunn,
////    godkjentPaVegneAvArbeidsgiver = avtale.godkjentPaVegneAvArbeidsgiver,
//)
