package no.nav.tiltak.datadeling

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import tools.jackson.module.kotlin.jacksonObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@ActiveProfiles(profiles = ["test"])
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = ["spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost/jwks"],
)
@DirtiesContext
@ContextConfiguration(initializers = [TiltakDatadelingApplicationTests.Companion.Initializer::class])
class SecurityConfigTest {

    @Value("\${local.server.port}")
    var port: Int = 0

    private val httpClient = HttpClient.newHttpClient()

    @Test
    fun `graphql krever token`() {
        val request = HttpRequest.newBuilder(URI.create("http://localhost:$port/graphql"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("""{"query":"{ avtale(avtaleNr: 1) { avtaleId } }"}"""))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        assertEquals(401, response.statusCode())
    }

    @Test
    fun `metadata for beskyttet ressurs oppgir ikke sertifikatbundne token`() {
        val request = HttpRequest.newBuilder(URI.create("http://localhost:$port/.well-known/oauth-protected-resource"))
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())
        val metadata = jacksonObjectMapper().readTree(response.body())

        assertEquals(200, response.statusCode())
        assertEquals(false, metadata.get("tls_client_certificate_bound_access_tokens")?.booleanValue())
    }
}
