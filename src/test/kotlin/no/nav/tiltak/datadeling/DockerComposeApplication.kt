package no.nav.tiltak.datadeling

import org.springframework.boot.runApplication

class DockerComposeApplication: TiltakDatadelingApplication()

fun main(args: Array<String>) {
    runApplication<DockerComposeApplication>(*args) {
        setAdditionalProfiles("testdata", "kafka", "dockercompose", "local")
    }
}
