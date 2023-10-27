package no.nav.tiltak.datadeling.domene

data class RefusjonKontaktperson(
    val refusjonKontaktpersonFornavn: String?,
    val refusjonKontaktpersonEtternavn: String?,
    val refusjonKontaktpersonTlf: String?,
    val ønskerVarslingOmRefusjon: Boolean?
)