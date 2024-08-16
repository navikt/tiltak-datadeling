package no.nav.tiltak.datadeling

import org.springframework.boot.runApplication

class LocalApplication: TiltakDatadelingApplication()

fun main(args: Array<String>) {
    TiltakPostgresContainer.getInstance().start()
    runApplication<LocalApplication>(*args) {
        setAdditionalProfiles("testdata", "kafka")
    }
}
