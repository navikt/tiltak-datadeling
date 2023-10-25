package no.nav.tiltak.datadeling.graphql

import graphql.GraphQLContext
import graphql.schema.DataFetchingEnvironment
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.domene.AvtaleRepository
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.time.LocalDateTime

@Controller
class GraphQLController(val avtaleRepository: AvtaleRepository) {

    @QueryMapping
    fun avtalerForPerson(context: GraphQLContext, @Argument personnummer: String): List<Avtale> {
        return avtaleRepository.hentAvtaleForPerson(personnummer)
    }

    @QueryMapping
    fun avtalerForBedrift(@Argument organisasjonsnummer: String): List<Avtale> {
        return avtaleRepository.hentAvtaleForBedrift(organisasjonsnummer)
    }

    @QueryMapping
    fun avtale(dataFetchingEnvironment: DataFetchingEnvironment, context: GraphQLContext, @Argument avtaleId: String): Avtale? {
        dataFetchingEnvironment
        val alleredeFunnetAvtale: Avtale? = context.get(avtaleId)
        return if (alleredeFunnetAvtale == null) {
            val hentetAvtale = avtaleRepository.hentAvtale(avtaleId).also {
                if (it != null) {
                    context.put(it.id, it)
                }
            }
            hentetAvtale
        } else {
            alleredeFunnetAvtale
        }
    }
}