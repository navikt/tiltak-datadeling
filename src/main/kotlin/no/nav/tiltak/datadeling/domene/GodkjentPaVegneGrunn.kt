package no.nav.tiltak.datadeling.domene


data class GodkjentPaVegneGrunn(
    val ikkeBankId: Boolean,
    val reservert: Boolean,
    val digitalKompetanse: Boolean,
    val arenaMigreringDeltaker: Boolean
)


