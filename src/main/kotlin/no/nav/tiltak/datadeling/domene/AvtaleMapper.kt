package no.nav.tiltak.datadeling.domene

import org.jooq.JSON
import org.springframework.stereotype.Component
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonMapperBuilder

@Component
class AvtaleMapper() {
    private val mapper: JsonMapper = jacksonMapperBuilder()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .build()

    fun tilAvtale(value: String): Avtale =
        mapper.readValue(value, Avtale::class.java).apply {
            this.rawJson = JSON.jsonOrNull(value)
        }

}
