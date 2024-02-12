package no.nav.tiltak.datadeling.db

import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.domene.Tiltakstype
import no.nav.tiltak.datadeling.graphql.AvtaleStatusGQL
import no.nav.tiltak.datadeling.graphql.map
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class AvtaleRepository {

    val log = LoggerFactory.getLogger(javaClass)
    var avtaler: ArrayList<Avtale> = ArrayList()

    fun hentAvtale(avtaleId: String?, avtaleNr: Int?): Avtale? {
        log.info("Henter avtale med avtaleId: $avtaleId, avtaleNr: $avtaleNr")
        return avtaler.sortedBy { it.versjon }
            .filter { if (avtaleId == null) true else it.avtaleId == UUID.fromString(avtaleId) }
            .filter { if (avtaleNr == null) true else it.avtaleNr == avtaleNr }
            .last()
    }

    fun hentAvtaleForTiltakstype(tiltakstype: Tiltakstype, status: AvtaleStatusGQL?): List<Avtale> {
        log.info("Henter avtaler for tiltakstype $tiltakstype")
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