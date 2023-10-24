package no.nav.tiltak.datadeling.domene

import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class AvtaleRepository {

    lateinit var avtaler: List<Avtale>

    @PostConstruct
    fun initialiseTulledata() {
        avtaler = listOf(
            Avtale("abc", 12, 1, LocalDateTime.now(), "8901", "12345"),
            Avtale("cda", 12, 1, LocalDateTime.now(), "9000", "54321"),
            Avtale("bas", 12, 1, LocalDateTime.now(), "9012", "23134")
        )
    }

    fun hentAvtaleForBedrift(bedriftnummer: String): List<Avtale> {
        println("HENTER AVTALER FOR BEDRIFT ${bedriftnummer}")
        return avtaler.filter { it.organisasjon == bedriftnummer }
    }

    fun hentAvtaleForPerson(personnummer: String): List<Avtale> {
        println("HENTER AVTALER FOR PERSON ${personnummer}")
        return avtaler.filter { it.deltakerFnr == personnummer }
    }

    fun hentAvtale(uuid: String): Avtale? {
        println("HENTER AVTALE ${uuid}")
        return avtaler.find { it.id == uuid }
    }
}