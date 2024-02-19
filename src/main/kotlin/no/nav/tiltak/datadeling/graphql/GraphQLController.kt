package no.nav.tiltak.datadeling.graphql

import graphql.language.Field
import graphql.schema.DataFetchingEnvironment
import no.nav.tiltak.datadeling.domene.Tiltakstype
import no.nav.tiltak.datadeling.opensearch.OpenSearchConnector
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class GraphQLController(val openSearchConnector: OpenSearchConnector) {

    @QueryMapping
    fun avtalerForPerson(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument personnummer: String
    ): List<AvtaleGQL> {
        return openSearchConnector.hentAvtale(mapOf("deltakerFnr" to personnummer), dataFetchingEnvironment.minimer())
    }

    @QueryMapping
    fun avtalerForBedrift(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument organisasjonsnummer: String
    ): List<AvtaleGQL> {
        return openSearchConnector.hentAvtale(
            mapOf("bedriftNr" to organisasjonsnummer),
            dataFetchingEnvironment.minimer()
        )
    }

    @QueryMapping
    fun avtale(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument avtaleId: String?,
        @Argument avtaleNr: Int?
    ): AvtaleGQL? {
        val parametere = mutableMapOf<String, String>().apply {
            if (avtaleId != null) this.put("avtaleId", avtaleId)
            if (avtaleNr != null) this.put("avtaleNr", avtaleNr.toString())
        }
        return openSearchConnector.hentAvtale(parametere, dataFetchingEnvironment.minimer()).first()
    }

    @QueryMapping
    fun avtalerForTiltakstype(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument tiltakstype: Tiltakstype
    ): List<AvtaleGQL> {
        return openSearchConnector.hentAvtale(
            mapOf("tiltakstype" to tiltakstype.name),
            dataFetchingEnvironment.minimer()
        )
    }

    @QueryMapping
    fun avtalerForStatus(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument status: AvtaleStatusGQL
    ): List<AvtaleGQL> {
        return openSearchConnector.hentAvtale(mapOf("avtaleStatus" to status.name), dataFetchingEnvironment.minimer())
    }

}

private fun DataFetchingEnvironment.minimer(): List<String> =
    this.field.selectionSet.selections.map { (it as Field).name }
