package no.nav.tiltak.datadeling.kafka

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.db.AvtaleRepository
import org.apache.kafka.common.TopicPartition
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.stereotype.Component

const val AVTALE_HENDELSE_COMPACT = "arbeidsgiver.tiltak-avtale-hendelse-compact"

@Profile("!local")
@Component
class TiltakHendelseKafkaKonsument(
    val avtaleRepository: AvtaleRepository
) : ConsumerSeekAware {
    val mapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        .registerModules(JavaTimeModule())

    @KafkaListener(topics = [AVTALE_HENDELSE_COMPACT])
    fun avtaleHendelseLytter(data: String) {
        try {
            val avtale = mapper.readValue(data, Avtale::class.java)
            avtaleRepository.save(avtale)
        } catch (e: Exception) {
            println("Feil oppstod ved henting av kafkamelding ${e.message}")
        }
    }

    /**
     * Når vi blir assigned til en partisjon, spol tilbake til start for å kverne igjennom all data på ny
     */
    @Override
    override fun onPartitionsAssigned(
        assignments: Map<TopicPartition, Long>,
        callback: ConsumerSeekAware.ConsumerSeekCallback
    ) {
        assignments.forEach {
            callback.seekToBeginning(it.key.topic(), it.key.partition())
        }
    }
}