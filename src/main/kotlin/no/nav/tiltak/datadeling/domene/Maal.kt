package no.nav.tiltak.datadeling.domene

import java.util.*


data class Maal (
    val id: UUID,
    val kategori: MaalKategori,
    val beskrivelse: String
)