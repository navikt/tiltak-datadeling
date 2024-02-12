package no.nav.tiltak.datadeling.opensearch

import org.springframework.boot.context.properties.ConfigurationProperties
import java.net.URI

//@Profile("prod")
@ConfigurationProperties(prefix = "tiltak-datadeling.opensearch")
data class OpenSearchProperties (
    //@Value("\${tiltak-datadeling.opensearch.url}")
    private var urlString: String = "",
    //@Value("\${tiltak-datadeling.opensearch.username}")
    var username: String = "",
    //@Value("\${tiltak-datadeling.opensearch.password}")
    var password: String = ""
) {
    val url = URI.create(urlString)
}
