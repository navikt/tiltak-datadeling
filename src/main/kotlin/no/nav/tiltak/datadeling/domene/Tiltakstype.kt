package no.nav.tiltak.datadeling.domene

enum class Tiltakstype(
    beskrivelse: String,
    behandlingstema: String,
    tiltakskodeArena: String?
) {
    ARBEIDSTRENING("Arbeidstrening", "ab0422", "ARBTREN"),
    MIDLERTIDIG_LONNSTILSKUDD("Midlertidig lønnstilskudd", "ab0336", "MIDLONTIL"),
    VARIG_LONNSTILSKUDD("Varig lønnstilskudd", "ab0337", "VARLONTIL"),
    MENTOR("Mentor", "ab0416", "MENTOR"),
    INKLUDERINGSTILSKUDD("Inkluderingstilskudd", "ab0417", "INKLUTILS"),
    SOMMERJOBB("Sommerjobb", "ab0450", null);
}
