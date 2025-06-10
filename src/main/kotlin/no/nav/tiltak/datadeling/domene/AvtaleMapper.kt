package no.nav.tiltak.datadeling.domene

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jooq.JSON
import org.springframework.stereotype.Component

@Component
class AvtaleMapper() {
    private val mapper: ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
        .registerModules(JavaTimeModule())

    fun tilAvtale(value: String): Avtale =
        mapper.readValue(value, Avtale::class.java).apply {
            this.rawJson = JSON.jsonOrNull(value)
        }

}
