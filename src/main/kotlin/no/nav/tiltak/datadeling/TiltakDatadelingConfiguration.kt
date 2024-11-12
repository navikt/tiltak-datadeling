package no.nav.tiltak.datadeling

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class TiltakDatadelingConfiguration {
    @Bean
    fun jsonMapper(): ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true)
        .registerModules(JavaTimeModule())

//    @Bean
//    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
//        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder -> wiringBuilder.scalar(ExtendedScalars.DateTime).scalar(ExtendedScalars.Date) }
//    }
//        return GraphQLScalarType("LocalDateTime", "DataTime scalar", object : Coercing<Any?, Any?> {
//            override fun serialize(input: Any): String? {
//                val date = input as LocalDateTime
//                return date.toString()
//            }
//
//            override fun parseValue(input: Any): LocalDateTime? {
//                return LocalDateTime.parse(input as String)
//            }
//
//            override fun parseLiteral(input: Any): LocalDateTime? {
//                return LocalDateTime.parse(input as String)
//            }
//        })
}
