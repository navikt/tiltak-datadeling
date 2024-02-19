package no.nav.tiltak.datadeling.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.graphql.map
import no.nav.tiltak.datadeling.opensearch.OpenSearchConnector
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.stereotype.Component

const val AVTALE_HENDELSE_COMPACT = "arbeidsgiver.tiltak-avtale-hendelse-compact"

//@Profile("!local")
@Component
@EnableKafka
class TiltakHendelseKafkaKonsument(
    val mapper: ObjectMapper,
    val openSearchConnector: OpenSearchConnector
) : ConsumerSeekAware {
    val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = [AVTALE_HENDELSE_COMPACT])
    fun avtaleHendelseLytter(data: String) {
        try {
            log.info("Mottatt melding på topic")
            val avtale = mapper.readValue(data, Avtale::class.java)
            openSearchConnector.save(map(avtale))
        } catch (e: Exception) {
            log.error("Feil oppstod ved henting av kafkamelding")
        }
    }
}