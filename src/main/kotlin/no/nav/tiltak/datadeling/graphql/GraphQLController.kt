package no.nav.tiltak.datadeling.graphql

import graphql.GraphQLContext
import graphql.language.Field
import graphql.schema.DataFetchingEnvironment
import no.nav.tiltak.datadeling.db.AvtaleRepository
import no.nav.tiltak.datadeling.domene.Tiltakstype
import no.nav.tiltak.datadeling.opensearch.OpenSearchConnector
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class GraphQLController(val avtaleRepository: AvtaleRepository, val openSearchConnector: OpenSearchConnector) {

    @QueryMapping
    fun avtalerForPerson(dataFetchingEnvironment: DataFetchingEnvironment, @Argument personnummer: String): List<AvtaleGQL> {
        val minimering = dataFetchingEnvironment.field.selectionSet.selections.map { (it as Field).name }
        return openSearchConnector.hentAvtale(mapOf("deltakerFnr" to personnummer), minimering)
    }

    @QueryMapping
    fun avtalerForBedrift(dataFetchingEnvironment: DataFetchingEnvironment, @Argument organisasjonsnummer: String): List<AvtaleGQL> {
        val minimering = dataFetchingEnvironment.field.selectionSet.selections.map { (it as Field).name }
        return openSearchConnector.hentAvtale(mapOf("bedriftNr" to organisasjonsnummer), minimering)
    }

    @QueryMapping
    fun avtale(context: GraphQLContext, @Argument avtaleId: String?, @Argument avtaleNr: Int?): AvtaleGQL? {
        return avtaleRepository.hentAvtale(avtaleId, avtaleNr)?.let { map(it) }
    }

    @QueryMapping
    fun avtalerForTiltakstype(@Argument tiltakstype: Tiltakstype, @Argument status: AvtaleStatusGQL?): List<AvtaleGQL> {
        return avtaleRepository.hentAvtaleForTiltakstype(tiltakstype, status).map { map(it) }
    }

}