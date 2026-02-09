package no.nav.tiltak.datadeling

import no.nav.tiltak.datadeling.security.NaisTokenIntrospector
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@Profile("!local")
class SecurityConfig(
    private val naisTokenIntrospector: NaisTokenIntrospector,
) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers("/internal/**", "/graphiql", "/graphiql/**").permitAll()
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2: OAuth2ResourceServerConfigurer<HttpSecurity?> ->
                oauth2
                    .opaqueToken { opaque ->
                        opaque.introspector(naisTokenIntrospector)
                    }
            }
        return http.build()
    }
}
