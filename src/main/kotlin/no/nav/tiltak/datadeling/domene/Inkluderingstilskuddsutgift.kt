package no.nav.tiltak.datadeling.domene

import java.util.*

data class Inkluderingstilskuddsutgift (
    val id: UUID,
    val beløp: Int,
    val type: InkluderingstilskuddsutgiftType
)