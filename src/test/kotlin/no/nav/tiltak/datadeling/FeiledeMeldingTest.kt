package no.nav.tiltak.datadeling

import no.nav.tiltak.datadeling.domene.AvtaleMapper
import no.nav.tiltak.datadeling.kafka.TiltakHendelseKafkaKonsument
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.support.Acknowledgment
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles(profiles = ["test", "local", "kafka"])
@SpringBootTest
@DirtiesContext
@ContextConfiguration(initializers = [TiltakDatadelingApplicationTests.Companion.Initializer::class])
class FeiledeMeldingTest {

    @Autowired
    lateinit var feiledeMeldingerRepository: FeiledeMeldingerRepository

    @Autowired
    lateinit var avtaleRepository: AvtaleRepository

    @Autowired
    lateinit var avtaleMapper: AvtaleMapper


    @Test
    fun `feilmelding lagres i egen logg`() {
        val lytter = TiltakHendelseKafkaKonsument(avtaleMapper, avtaleRepository, feiledeMeldingerRepository)
        var acked = false;

        lytter.avtaleHendelseLytter(
            ConsumerRecord("test", 0, 0, "avtale-melding", "poison-pill"),
            object : Acknowledgment {
                override fun acknowledge() {
                    acked = true
                }
            }
        )

        Assertions.assertTrue(acked)
        Assertions.assertEquals(1, feiledeMeldingerRepository.hentFeiledeMeldinger().size)
    }
}
