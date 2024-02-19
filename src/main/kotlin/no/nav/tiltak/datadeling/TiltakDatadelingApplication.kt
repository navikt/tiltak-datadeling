package no.nav.tiltak.datadeling

import no.nav.security.token.support.spring.api.EnableJwtTokenValidation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess

@SpringBootApplication
@EnableJwtTokenValidation
class TiltakDatadelingApplication

fun main(args: Array<String>) {
    runApplication<TiltakDatadelingApplication>(*args) {
        if (System.getenv("MILJO") == null) {
            println("Kan ikke startes uten miljøvariabel MILJO. Lokalt kan TiltakDatadelingApplication kjøres.")
            exitProcess(1)
        }
        setAdditionalProfiles(System.getenv("MILJO"))
    }
}
