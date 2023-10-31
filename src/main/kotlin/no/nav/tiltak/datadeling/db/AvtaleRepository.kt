package no.nav.tiltak.datadeling.db

import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.domene.Tiltakstype
import no.nav.tiltak.datadeling.graphql.AvtaleStatusGQL
import no.nav.tiltak.datadeling.graphql.map
import org.springframework.stereotype.Repository
import java.util.*

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

    fun hentAvtale(uuid: String?, avtaleNr: Int?): Avtale? {
        println("HENTER AVTALE ${uuid}")
        return avtaler.sortedBy { it.versjon }
            .filter { if (uuid == null) true else it.avtaleId == UUID.fromString(uuid) }
            .filter { if (avtaleNr == null) true else it.avtaleNr == avtaleNr }
            .last()

    }

    fun hentAvtaleForTiltakstype(tiltakstype: Tiltakstype, status: AvtaleStatusGQL?): List<Avtale> {
        println("HENTER AVTALER FOR TILTAKSTYPE $tiltakstype")
        if (status != null) {
            return avtaler.filter {
                it.tiltakstype == tiltakstype && map(it.avtaleStatus) == status
            }
        } else {
            return avtaler.filter { it.tiltakstype == tiltakstype }

        }
    }

    fun save(avtale: Avtale) {
        this.avtaler.add(avtale)
    }
}