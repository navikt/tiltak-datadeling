package no.nav.tiltak.datadeling.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.tiltak.datadeling.AvtaleRepository
import no.nav.tiltak.datadeling.domene.Avtale
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.atomic.AtomicBoolean

@Profile("kafka")
@Component
@EnableKafka
class TiltakHendelseKafkaKonsument(
    val mapper: ObjectMapper,
    val avtaleRepository: AvtaleRepository
) : ConsumerSeekAware {
    val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${tiltak-datadeling.kafka.topic}"])
    fun avtaleHendelseLytter(acknowledgment: Acknowledgment, data: String) {
        try {
            log.info("Mottatt melding på topic")
            val avtale = mapper.readValue(data, Avtale::class.java)
            avtaleRepository.save(avtale)
            acknowledgment.acknowledge()
        } catch (ex: Exception) {
            log.error("Feil oppstod ved henting av kafkamelding", ex)
            acknowledgment.nack(Duration.ofSeconds(5))
        }
    }

    /**
     * Når vi blir assigned til en partisjon, spol tilbake til start for å kverne igjennom all data på ny
     */
    override fun onPartitionsAssigned(assignments: MutableMap<TopicPartition, Long>, callback: ConsumerSeekAware.ConsumerSeekCallback) {
        if (isReset.compareAndSet(false, true)) {
            log.info("Resetter offset for $assignments")
            callback.seekToBeginning(assignments.keys)
        }
    }
}

val isReset = AtomicBoolean(false)
