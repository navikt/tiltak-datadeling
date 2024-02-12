package no.nav.tiltak.datadeling

import jakarta.annotation.PostConstruct
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Profile("testdata")
@Component
class LastTestData(
    @Value("\${tiltak-datadeling.kafka.topic}")
    val topic: String,
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    @PostConstruct
    fun init() {
        val data = this::class.java.classLoader.getResourceAsStream("test-data.jsonl")?.bufferedReader()?.readLines()
            ?: emptyList()
        data.forEach {
            kafkaTemplate.send(ProducerRecord(topic, it))
        }
    }
}