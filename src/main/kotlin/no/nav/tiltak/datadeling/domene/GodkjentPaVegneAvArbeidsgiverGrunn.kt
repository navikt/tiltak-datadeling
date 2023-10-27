package no.nav.tiltak.datadeling.domene

data class GodkjentPaVegneAvArbeidsgiverGrunn (
    val klarerIkkeGiFaTilgang: Boolean,
    val vetIkkeHvemSomKanGiTilgang: Boolean,
    val farIkkeTilgangPersonvern: Boolean,
    val arenaMigreringArbeidsgiver: Boolean
)