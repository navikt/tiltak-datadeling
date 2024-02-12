package no.nav.tiltak.datadeling

import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<TiltakDatadelingApplication>(*args) {
        setAdditionalProfiles("dockercompose", "testdata")
    }
}
