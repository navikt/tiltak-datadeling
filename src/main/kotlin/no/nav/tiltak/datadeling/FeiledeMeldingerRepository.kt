package no.nav.tiltak.datadeling

import no.nav.tiltak.datadeling.db.tables.records.FeiledeMeldingerRecord
import no.nav.tiltak.datadeling.db.tables.references.FEILEDE_MELDINGER
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.jooq.impl.DefaultDSLContext
import org.springframework.stereotype.Component

@Component
class FeiledeMeldingerRepository(
    private val dslContext: DefaultDSLContext
) {
    fun save(record: ConsumerRecord<String, String>, e: Exception) {
        dslContext.insertInto(FEILEDE_MELDINGER)
            .set(FEILEDE_MELDINGER.KAFKA_KEY, record.key())
            .set(FEILEDE_MELDINGER.PAYLOAD, record.value())
            .set(FEILEDE_MELDINGER.FEILMELDING, e.message)
            .execute()
    }

    fun hentFeiledeMeldinger(): List<FeiledeMeldingerRecord> {
        return dslContext.selectFrom(FEILEDE_MELDINGER)
            .map{it};
    }
}
