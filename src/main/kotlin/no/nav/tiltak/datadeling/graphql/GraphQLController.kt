package no.nav.tiltak.datadeling.graphql

import graphql.GraphQLContext
import no.nav.tiltak.datadeling.db.AvtaleRepository
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.domene.AvtaleStatus
import no.nav.tiltak.datadeling.domene.Tiltakstype
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class GraphQLController(val avtaleRepository: AvtaleRepository) {

    @QueryMapping
    fun avtalerForPerson(context: GraphQLContext, @Argument personnummer: String): List<AvtaleGQL> {
        return avtaleRepository.hentAvtaleForPerson(personnummer).map { map(it) }
    }

    @QueryMapping
    fun avtalerForBedrift(@Argument organisasjonsnummer: String): List<AvtaleGQL> {
        return avtaleRepository.hentAvtaleForBedrift(organisasjonsnummer).map { map(it) }
    }

    @QueryMapping
    fun avtale(context: GraphQLContext, @Argument avtaleId: String): AvtaleGQL? {
        val alleredeFunnetAvtale: Avtale? = context.get(avtaleId)
        val avtale = if (alleredeFunnetAvtale == null) {
            val hentetAvtale = avtaleRepository.hentAvtale(avtaleId).also {
                if (it != null) {
                    context.put(it.avtaleId, it)
                }
            }
            hentetAvtale
        } else {
            alleredeFunnetAvtale
        }
        return avtale?.let { map(it) }
    }

    @QueryMapping
    fun avtalerForTiltakstype(@Argument tiltakstype: Tiltakstype, @Argument status: AvtaleStatusGQL?): List<AvtaleGQL> {
        return avtaleRepository.hentAvtaleForTiltakstype(tiltakstype, status).map { map(it) }
    }

}