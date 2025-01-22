package no.nav.tiltak.datadeling

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
class TiltakDatadelingApplication

fun main(args: Array<String>) {
    runApplication<TiltakDatadelingApplication>(*args) {
        if (System.getenv("MILJO") == null) {
            println("Kan ikke startes uten miljøvariabel MILJO. Lokalt kan LocalApplication kjøres.")
            exitProcess(1)
        }
        setAdditionalProfiles(System.getenv("MILJO"), "kafka")
    }
}
