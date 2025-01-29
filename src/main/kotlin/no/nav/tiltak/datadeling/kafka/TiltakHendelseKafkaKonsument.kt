package no.nav.tiltak.datadeling.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.tiltak.datadeling.AvtaleRepository
import no.nav.tiltak.datadeling.FeiledeMeldingerRepository
import no.nav.tiltak.datadeling.domene.Avtale
import no.nav.tiltak.datadeling.toOsloOffset
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
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
    @Qualifier("jsonMapper")
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
            log.info("Lagrer avtale {}, endret {}", avtale.avtaleId, avtale.sistEndret)
            val lagretAvtale = avtaleRepository.save(avtale)
            if (lagretAvtale == null) {
                log.info("Avtale {} ble ikke oppdatert", avtale.avtaleId)
            } else if (lagretAvtale.endretTidspunkt == avtale.sistEndret.toOsloOffset()) {
                log.info("Avtale {} ble oppdatert, samme endret-tidspunkt", avtale.avtaleId)
            }
        } catch (ex: Exception) {
            log.error("Feil oppstod ved henting av kafkamelding", ex)
            feiledeMeldingerRepository.save(record, ex)

        } finally {
            acknowledgment.acknowledge()
        }
    }

    override fun onPartitionsAssigned(assignments: MutableMap<TopicPartition, Long>, callback: ConsumerSeekAware.ConsumerSeekCallback) {
        super.onPartitionsAssigned(assignments, callback)
        callback.seekToBeginning(assignments.keys)
    }
}
