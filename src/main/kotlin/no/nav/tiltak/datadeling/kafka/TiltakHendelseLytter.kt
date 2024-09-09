package no.nav.tiltak.datadeling.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import no.nav.tiltak.datadeling.AvtaleRepository
import no.nav.tiltak.datadeling.domene.Avtale
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AbstractConsumerSeekAware
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

    @PostConstruct
    fun init() {
        log.info("SEEKER TIL START")
        log.info("Assignments: ${callbacksAndTopics.map { it.value }}")
        seekToBeginning()
    }
}
