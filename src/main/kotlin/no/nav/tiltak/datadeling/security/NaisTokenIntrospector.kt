package no.nav.tiltak.datadeling.security

import com.fasterxml.jackson.annotation.JsonProperty
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import java.time.Instant

@Profile("!local")
@Component
class NaisTokenIntrospector(
    @Value("\${NAIS_TOKEN_INTROSPECTION_ENDPOINT}") private val endpoint: String,
    private val restClientBuilder: RestClient.Builder,
) : OpaqueTokenIntrospector {
    val log = LoggerFactory.getLogger(javaClass)
    private val restClient: RestClient = restClientBuilder.build()

    override fun introspect(token: String): OAuth2AuthenticatedPrincipal {
        log.info("Token-introspekt")
        val response = try {
            restClient.post()
                .uri(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .body(TokenIntrospectionRequest(identityProvider = "entra_id", token = token))
                .retrieve()
                .body(MutableMap::class.java)
        } catch (ex: RestClientException) {
            log.error("Feil ved token introspeksjon", ex)
            throw OAuth2IntrospectionException("Token introspection failed", ex)
        }

        val attributes = (response
            ?.entries
            ?.associate { it.key.toString() to it.value }
            ?: emptyMap()).toMutableMap()

        convertNumericDatesToInstant(attributes)

        val active = attributes["active"] as? Boolean
        if (active == false) {
            log.error("Token is not active")
            throw OAuth2IntrospectionException("Token is not active")
        }
        log.info("Er vurdert aktiv: $active")

        return OAuth2IntrospectionAuthenticatedPrincipal(attributes, emptyList())
    }

    private fun convertNumericDatesToInstant(attributes: MutableMap<String, Any?>) {
        val dateKeys = listOf("exp", "iat", "nbf")
        for (key in dateKeys) {
            val value = attributes[key]
            if (value is Number) {
                attributes[key] = Instant.ofEpochSecond(value.toLong())
            }
        }
    }

    private fun extractAuthorities(attributes: Map<String, Any?>): Collection<GrantedAuthority> {
        val authorities = mutableSetOf<GrantedAuthority>()
        val groups = attributes["groups"]
        if (groups is Collection<*>) {
            groups.filterIsInstance<String>()
                .map { SimpleGrantedAuthority("GROUP_$it") }
                .forEach { authorities.add(it) }
        }
        return authorities
    }
}

data class TokenIntrospectionRequest(
    @field:JsonProperty("identity_provider") val identityProvider: String,
    val token: String,
)

