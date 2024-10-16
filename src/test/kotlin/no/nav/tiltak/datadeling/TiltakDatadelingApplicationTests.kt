package no.nav.tiltak.datadeling

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.ClassRule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.PostgreSQLContainer


@ActiveProfiles(profiles = ["test", "local", "kafka"])
@SpringBootTest
@DirtiesContext
//@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"], topics = ["test-topic"])
@ContextConfiguration(initializers = [TiltakDatadelingApplicationTests.Companion.Initializer::class])
class TiltakDatadelingApplicationTests {

    @Autowired
    lateinit var producer: KafkaProducer<String, String>

    @Autowired
    lateinit var avtaleRepository: AvtaleRepository

    @Value("\${tiltak-datadeling.kafka.topic}")
    lateinit var topic: String

    @Test
    fun `kan konsumere og indeksere avtaler`() {
        val testData = hentTestdata().take(20)
        testData.forEach {
            producer.send(ProducerRecord(topic, it))
        }
        Thread.sleep(10000)

        assertEquals(testData.count(), avtaleRepository.count())
    }

    fun hentTestdata(): List<String> {
        return this::class.java.classLoader.getResourceAsStream("test-data.jsonl")?.bufferedReader()?.readLines()
            ?: emptyList()
    }

    companion object {
        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
                postgreSQLContainer.start()
                TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.jdbcUrl,
                    "spring.datasource.username=" + postgreSQLContainer.username,
                    "spring.datasource.password=" + postgreSQLContainer.password
                ).applyTo(configurableApplicationContext.environment)
            }
        }

        @JvmField
        @ClassRule
        var postgreSQLContainer: PostgreSQLContainer<TiltakPostgresContainer> = TiltakPostgresContainer.getInstance()
    }
}
