package no.nav.tiltak.datadeling.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.tiltak.datadeling.AvtaleRepository
import no.nav.tiltak.datadeling.FeiledeMeldingerRepository
import no.nav.tiltak.datadeling.domene.Avtale
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.AbstractConsumerSeekAware
import org.springframework.kafka.listener.ConsumerSeekAware
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Profile("kafka")
@Component
@EnableKafka
class TiltakHendelseKafkaKonsument(
    val mapper: ObjectMapper,
    val avtaleRepository: AvtaleRepository,
    val feiledeMeldingerRepository: FeiledeMeldingerRepository
) : AbstractConsumerSeekAware() {
    val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${tiltak-datadeling.kafka.topic}"])
    fun avtaleHendelseLytter(record: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        try {
            log.info("Mottatt melding på topic")
            val avtale = mapper.readValue(record.value(), Avtale::class.java)
            avtaleRepository.save(avtale)
            acknowledgment.acknowledge()
        } catch (ex: Exception) {
            log.error("Feil oppstod ved henting av kafkamelding", ex)
            feiledeMeldingerRepository.save(record, ex)
            acknowledgment.acknowledge()
        }
    }

    override fun onPartitionsAssigned(assignments: MutableMap<TopicPartition, Long>, callback: ConsumerSeekAware.ConsumerSeekCallback) {
        super.onPartitionsAssigned(assignments, callback)
        callback.seekToBeginning(assignments.keys)
    }
}
