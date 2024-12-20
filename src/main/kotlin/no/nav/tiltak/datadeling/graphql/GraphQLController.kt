package no.nav.tiltak.datadeling.graphql

import graphql.language.Field
import graphql.schema.DataFetchingEnvironment
import no.nav.tiltak.datadeling.AvtaleRepository
import no.nav.tiltak.datadeling.domene.Tiltakstype
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class GraphQLController(val avtaleRepository: AvtaleRepository) {

    @QueryMapping
    fun avtalerForPerson(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument personnummer: String
    ): List<AvtaleGQL> {
        return avtaleRepository.hentAvtale(mapOf("deltakerFnr" to personnummer), dataFetchingEnvironment.minimer())
    }

    @QueryMapping
    fun avtalerForBedrift(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument organisasjonsnummer: String
    ): List<AvtaleGQL> {
        return avtaleRepository.hentAvtale(
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
        return avtaleRepository.hentAvtale(parametere, dataFetchingEnvironment.minimer()).firstOrNull()
    }

    @QueryMapping
    fun avtalerForTiltakstype(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument tiltakstype: Tiltakstype
    ): List<AvtaleGQL> {
        return avtaleRepository.hentAvtale(
            mapOf("tiltakstype" to tiltakstype.name),
            dataFetchingEnvironment.minimer()
        )
    }

    @QueryMapping
    fun avtalerForStatus(
        dataFetchingEnvironment: DataFetchingEnvironment,
        @Argument status: AvtaleStatusGQL
    ): List<AvtaleGQL> {
        return avtaleRepository.hentAvtale(mapOf("avtaleStatus" to map(status).name), dataFetchingEnvironment.minimer())
    }

}

private fun DataFetchingEnvironment.minimer(): List<String> =
    this.field.selectionSet.selections.map { (it as Field).name }
