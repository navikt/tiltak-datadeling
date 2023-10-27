package no.nav.tiltak.datadeling.db

import no.nav.tiltak.datadeling.domene.Avtale
import org.springframework.stereotype.Repository
import java.util.*
import kotlin.collections.ArrayList

@Repository
class AvtaleRepository {

    var avtaler: ArrayList<Avtale> = ArrayList()

    fun hentAvtaleForBedrift(bedriftnummer: String): List<Avtale> {
        println("HENTER AVTALER FOR BEDRIFT ${bedriftnummer}")
        return avtaler.filter { it.bedriftNr == bedriftnummer }
    }

    fun hentAvtaleForPerson(personnummer: String): List<Avtale> {
        println("HENTER AVTALER FOR PERSON ${personnummer}")
        return avtaler.filter { it.deltakerFnr == personnummer }
    }

    fun hentAvtale(uuid: String): Avtale? {
        println("HENTER AVTALE ${uuid}")
        return avtaler.find { it.avtaleId == UUID.fromString(uuid) }
    }

    fun save(avtale: Avtale) {
        this.avtaler.add(avtale)
    }
}