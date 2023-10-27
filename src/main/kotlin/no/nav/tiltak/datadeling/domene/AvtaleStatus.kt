package no.nav.tiltak.datadeling.domene

enum class AvtaleStatus(tekst: String) {
    ANNULLERT("Annullert"),
    AVBRUTT("Avbrutt"),
    PÅBEGYNT("Påbegynt"),
    MANGLER_GODKJENNING("Mangler godkjenning"),
    KLAR_FOR_OPPSTART("Klar for oppstart"),
    GJENNOMFØRES("Gjennomføres"),
    AVSLUTTET("Avsluttet");
}
