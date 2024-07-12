package no.nav.tiltak.datadeling

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.time.Duration
import java.util.*


@ActiveProfiles(profiles = ["test", "local"])
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"], topics = ["test-topic"])
class TiltakDatadelingApplicationTests {

    @Autowired
    lateinit var consumer: KafkaConsumer<String, String>

    @Autowired
    lateinit var producer: KafkaProducer<String, String>

    @Autowired
    lateinit var avtaleRepository: AvtaleRepository

    @Value("\${tiltak-datadeling.kafka.topic}")
    lateinit var topic: String

    @BeforeEach
    fun setup() {
        consumer.subscribe(listOf(topic))
    }

    @AfterEach
    fun teardown() {
        consumer.unsubscribe()
    }

    @Test
    @Throws(Exception::class)
    fun givenKafkaDockerContainer_whenSendingWithSimpleProducer_thenMessageReceived() {
        val data = "Sending with our own simple KafkaProducer"
        producer.send(ProducerRecord(topic, data)).get()
        val messageConsumed = consumer.poll(Duration.ofSeconds(10)).records(topic).map { it.value() }

        assertTrue(messageConsumed.contains(data))
    }

    @Test
    fun `kan konsumere og indeksere avtaler`() {
        val testData = hentTestdata().take(20).apply {
            this.forEach {
                producer.send(ProducerRecord(topic, it))
            }
        }
        Thread.sleep(10000)

        assertEquals(testData.count().toLong(), avtaleRepository.count())
    }

    fun hentTestdata(): List<String> {
        return this::class.java.classLoader.getResourceAsStream("test-data.jsonl")?.bufferedReader()?.readLines()
            ?: emptyList()
    }
}
