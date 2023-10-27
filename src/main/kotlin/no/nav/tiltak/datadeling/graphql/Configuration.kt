package no.nav.tiltak.datadeling.graphql

import graphql.language.StringValue
import graphql.scalars.ExtendedScalars
import graphql.schema.*
import graphql.schema.idl.RuntimeWiring
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import java.time.LocalDateTime


val localDateTimeScalar = GraphQLScalarType.newScalar()
    .name("LocalDateTime")
    .description("A DateTime-format without a time zone component")
    .coercing(object : Coercing<Any?, Any?> {
        @Deprecated("Deprecated in Java")
        override fun serialize(dataFetcherResult: Any): Any {
            if (dataFetcherResult is LocalDateTime) {
                return dataFetcherResult.toString()
            } else {
                throw CoercingSerializeException("Data could not be serialized to a LocalDateTime")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun parseValue(input: Any): Any? {
            if (input is String) {
                return LocalDateTime.parse(input)
            } else {
                throw CoercingParseValueException("Input could not be parsed to a LocalDateTime")
            }
        }

        @Deprecated("Deprecated in Java")
        override fun parseLiteral(input: Any): Any {
            if (input is StringValue) {
                return LocalDateTime.parse(input.value)
            }
            throw CoercingParseLiteralException(
                "Value is not a valid localdatetime : '" + java.lang.String.valueOf(input) + "'"
            )
        }
    })
    .build();

@Configuration
class GraphQlConfig {
    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { wiringBuilder: RuntimeWiring.Builder ->
            wiringBuilder
                .scalar(ExtendedScalars.DateTime)
                .scalar(localDateTimeScalar)
                .scalar(ExtendedScalars.Date)
        }
    }
}