package no.nav.tiltak.datadeling

import org.springframework.boot.runApplication

//@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"], topics = ["tiltak-datadeling-test"])
class LocalApplication: TiltakDatadelingApplication()

fun main(args: Array<String>) {
    TiltakPostgresContainer.getInstance().start()
    runApplication<LocalApplication>(*args) {
        setAdditionalProfiles("testdata", "kafka")
    }
}
