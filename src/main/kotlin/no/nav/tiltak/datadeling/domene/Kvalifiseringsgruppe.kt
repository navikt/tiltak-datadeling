package no.nav.tiltak.datadeling.domene

enum class Kvalifiseringsgruppe {
    BATT,   // Personen har nedsatt arbeidsevne og har et identifisert behov for kvalifisering og/eller tilrettelegging. Aktivitetsplan skal utformes.
    BFORM,  // Personen har moderat bistandsbehov
    VARIG,  // Personen har varig nedsatt arbeidsevne
    BKART,  // Personen har behov for arbeidsevnevurdering
    IKVAL,  // Personen har behov for ordinær bistand
    IVURD,  // Ikke vurdert
    KAP11,  // Rettigheter etter Ftrl. Kapittel 11
    OPPFI,  // Helserelatert arbeidsrettet oppfølging i NAV
    VURDI,  // Sykmeldt, oppfølging på arbeidsplassen
    VURDU;  // Sykmeldt uten arbeidsgiver
}
