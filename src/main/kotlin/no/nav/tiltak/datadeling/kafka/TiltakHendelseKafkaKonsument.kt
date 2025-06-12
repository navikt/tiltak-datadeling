package no.nav.tiltak.datadeling.kafka

import no.nav.tiltak.datadeling.AvtaleRepository
import no.nav.tiltak.datadeling.FeiledeMeldingerRepository
import no.nav.tiltak.datadeling.domene.AvtaleMapper
import no.nav.tiltak.datadeling.toOsloOffset
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Profile("kafka")
@Component
@EnableKafka
class TiltakHendelseKafkaKonsument(
    val avtaleMapper: AvtaleMapper,
    val avtaleRepository: AvtaleRepository,
    val feiledeMeldingerRepository: FeiledeMeldingerRepository
) {
    val log = LoggerFactory.getLogger(javaClass)

    @KafkaListener(topics = ["\${tiltak-datadeling.kafka.topic}"])
    fun avtaleHendelseLytter(record: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        try {
            log.info("Mottatt melding på topic")
            val avtale = avtaleMapper.tilAvtale(record.value())
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
}
