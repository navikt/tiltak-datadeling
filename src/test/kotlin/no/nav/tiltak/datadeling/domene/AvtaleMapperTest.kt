package no.nav.tiltak.datadeling.domene

import org.jooq.JSON
import org.junit.Test
import org.junit.jupiter.api.Assertions

class AvtaleMapperTest {
    private val avtaleMapper = AvtaleMapper()

    @Test
    fun testAvtaleMapper() {
        val json = """
            {"hendelseType":"ENDRET","avtaleStatus":"MANGLER_GODKJENNING","deltakerFnr":"28109624685","mentorFnr":null,"bedriftNr":"910825518","veilederNavIdent":"Z992785","tiltakstype":"INKLUDERINGSTILSKUDD","opprettetTidspunkt":"2022-08-30T12:05:39.70434","avtaleId":"a3592491-c659-4262-b3ec-1d920527ff74","avtaleNr":426,"sistEndret":"2023-11-07T13:57:45.633871571Z","annullertTidspunkt":null,"annullertGrunn":null,"slettemerket":false,"opprettetAvArbeidsgiver":false,"enhetGeografisk":"0805","enhetsnavnGeografisk":"NAV Porsgrunn","enhetOppfolging":"0805","enhetsnavnOppfolging":"NAV Porsgrunn","godkjentForEtterregistrering":false,"kvalifiseringsgruppe":"BFORM","formidlingsgruppe":"ARBS","tilskuddPeriode":[],"feilregistrert":false,"versjon":1,"deltakerFornavn":"Cariñoso","deltakerEtternavn":"Møbelforretning","deltakerTlf":"123456789","bedriftNavn":"MAURA OG KOLBU REGNSKAP","arbeidsgiverFornavn":"hh","arbeidsgiverEtternavn":"hh","arbeidsgiverTlf":"12345678","veilederFornavn":"nnm,nm,","veilederEtternavn":"jjjjj","veilederTlf":"12345678","oppfolging":"ggggg","tilrettelegging":"ggg","startDato":"2023-11-07","sluttDato":"2023-11-30","stillingprosent":null,"journalpostId":null,"arbeidsoppgaver":null,"stillingstittel":null,"stillingStyrk08":null,"stillingKonseptId":null,"antallDagerPerUke":null,"refusjonKontaktperson":null,"mentorFornavn":null,"mentorEtternavn":null,"mentorOppgaver":null,"mentorAntallTimer":null,"mentorTimelonn":null,"mentorTlf":null,"arbeidsgiverKontonummer":null,"lonnstilskuddProsent":null,"manedslonn":null,"feriepengesats":null,"arbeidsgiveravgift":null,"harFamilietilknytning":false,"familietilknytningForklaring":null,"feriepengerBelop":null,"otpSats":null,"otpBelop":null,"arbeidsgiveravgiftBelop":null,"sumLonnsutgifter":null,"sumLonnstilskudd":null,"manedslonn100pst":null,"sumLønnstilskuddRedusert":null,"datoForRedusertProsent":null,"stillingstype":null,"maal":[],"inkluderingstilskuddsutgift":[{"id":"d98c330e-a13b-4445-a011-38da9a101f28","beløp":9999,"type":"PROGRAMVARE"}],"inkluderingstilskuddBegrunnelse":"lkjhgf","inkluderingstilskuddTotalBeløp":9999,"godkjentAvDeltaker":null,"godkjentTaushetserklæringAvMentor":null,"godkjentAvArbeidsgiver":null,"godkjentAvVeileder":null,"godkjentAvBeslutter":null,"avtaleInngått":null,"ikrafttredelsestidspunkt":null,"godkjentAvNavIdent":null,"godkjentAvBeslutterNavIdent":null,"enhetKostnadssted":null,"enhetsnavnKostnadssted":null,"godkjentPaVegneGrunn":null,"godkjentPaVegneAv":false,"godkjentPaVegneAvArbeidsgiverGrunn":null,"godkjentPaVegneAvArbeidsgiver":false,"innholdType":"INNGÅ","utførtAv":"16120101181","utførtAvRolle":"ARBEIDSGIVER"}
        """
        val avtale = avtaleMapper.tilAvtale(json)

        Assertions.assertEquals(avtale.rawJson, JSON.jsonOrNull(json))
    }
}
