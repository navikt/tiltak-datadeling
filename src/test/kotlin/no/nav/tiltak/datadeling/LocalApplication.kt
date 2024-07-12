package no.nav.tiltak.datadeling

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
//@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"], topics = ["tiltak-datadeling-test"])
class LocalApplication

fun main(args: Array<String>) {
    runApplication<LocalApplication>(*args) {
        setAdditionalProfiles("testdata", "kafka")
    }
}
