package no.nav.tiltak.datadeling.domene

enum class Formidlingsgruppe() {
    ARBS,  // Person er tilgjengelig for alt søk etter   arbeidskraft, ordinær og vikar
    IARBS, // Person er ikke tilgjengelig for søk etter arbeidskraft
    IJOBS, // Jobbskifter som er inaktivert fra nav.no
    ISERV, // Inaktivering, person mottar ikke bistand fra NAV
    JOBBS, // Personen er ikke tilgjengelig for søk
    PARBS, // Personen fra nav.no som ønsker å bli arbeidssøker, men som enda ikke er   verifisert
    RARBS; //Person som er reaktivert fra nav.no
}
