package no.nav.tiltak.datadeling.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.tiltak.datadeling.AvtaleRepository
import no.nav.tiltak.datadeling.domene.Avtale
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AbstractConsumerSeekAware
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component
import java.time.Duration

@Profile("kafka")
@Component
@EnableKafka
class TiltakHendelseKafkaKonsument(
    val mapper: ObjectMapper,
    val avtaleRepository: AvtaleRepository
) : AbstractConsumerSeekAware() {
    val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${tiltak-datadeling.kafka.topic}"])
    fun avtaleHendelseLytter(data: String, acknowledgment: Acknowledgment) {
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

    override fun onPartitionsAssigned(assignments: MutableMap<TopicPartition, Long>, callback: ConsumerSeekAware.ConsumerSeekCallback) {
        super.onPartitionsAssigned(assignments, callback)
        callback.seekToBeginning(assignments.keys)
    }
}
