package no.nav.tiltak.datadeling.opensearch

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.BasicCredentialsProvider
import org.opensearch.client.RestClient
import org.opensearch.client.json.jackson.JacksonJsonpMapper
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.transport.OpenSearchTransport
import org.opensearch.client.transport.rest_client.RestClientTransport
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@Profile(value = ["prod", "dockercompose"])
@Configuration
@EnableConfigurationProperties(OpenSearchProperties::class)
class OpenSearchConfig {
    @Bean
    fun openSearchClient(openSearchProperties: OpenSearchProperties, objectMapper: ObjectMapper) =
        buildOpenSearchClient(openSearchProperties, objectMapper)
}

fun buildOpenSearchClient(openSearchProps: OpenSearchProperties, mapper: ObjectMapper): OpenSearchClient {
    val host = HttpHost(openSearchProps.url.host, openSearchProps.url.port, openSearchProps.url.scheme)
    val credentialsProvider = BasicCredentialsProvider()
    credentialsProvider.setCredentials(
        AuthScope(host),
        UsernamePasswordCredentials(openSearchProps.username, openSearchProps.password)
    )
    val sslcontext = SSLContext.getInstance("TLS")
    sslcontext.init(null, arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(arg0: Array<X509Certificate?>?, arg1: String?) {
        }

        override fun checkServerTrusted(arg0: Array<X509Certificate?>?, arg1: String?) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls(0)
        }
    }), SecureRandom())
    //Initialize the client with SSL and TLS enabled
    val restClient: RestClient =
        RestClient.builder(host).setHttpClientConfigCallback { httpClientBuilder ->
            httpClientBuilder
                .setSSLContext(sslcontext)
                .setSSLHostnameVerifier(NoopHostnameVerifier()).setDefaultCredentialsProvider(
                    credentialsProvider
                )
        }.build()
    val transport: OpenSearchTransport = RestClientTransport(restClient, JacksonJsonpMapper(mapper))

    return OpenSearchClient(transport)
}