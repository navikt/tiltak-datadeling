package no.nav.tiltak.datadeling.opensearch

import jakarta.annotation.PostConstruct
import no.nav.tiltak.datadeling.graphql.AvtaleGQL
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.client.opensearch._types.OpenSearchException
import org.opensearch.client.opensearch._types.query_dsl.Query
import org.opensearch.client.opensearch.core.CountRequest
import org.opensearch.client.opensearch.core.IndexRequest
import org.opensearch.client.opensearch.core.SearchRequest
import org.opensearch.client.opensearch.indices.CreateIndexRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class OpenSearchConnector(
    @Value("\${tiltak-datadeling.opensearch.index}")
    private val openSearchIndex: String,
    private val openSearchClient: OpenSearchClient
) {
    @PostConstruct
    fun init() {
        opprettIndeks(openSearchIndex)
        opprettIndeks("${openSearchIndex}-historikk")
    }

    private fun opprettIndeks(indeksnavn: String) {
        try {
            openSearchClient.indices().create(CreateIndexRequest.Builder().index(indeksnavn).build())
        } catch (e: OpenSearchException) {
            if (e.response().error().type() != "resource_already_exists_exception") {
                throw e
            }
        }
    }

    fun count() = openSearchClient.count(
        CountRequest.Builder().index(openSearchIndex).build()
    ).count()

    fun hentAvtale(feltverdier: Map<String, String>, minimering: List<String>): List<AvtaleGQL> =
        openSearchClient.search(
            SearchRequest.of { s ->
                s.index(listOf(openSearchIndex))
                    .query(Query.of { q ->
                        q.match { m ->
                            feltverdier.entries.fold(m) { acc, entry ->
                                acc.field(entry.key).query { q -> q.stringValue(entry.value) }
                            }
                        }
                    }).let { request ->
                        if (minimering.isNotEmpty()) {
                            request.source { s -> s.filter { f -> f.includes(minimering) } }
                        } else request
                    }
            }, AvtaleGQL::class.java
        ).hits().hits().map { it.source()!! }

    fun save(avtale: AvtaleGQL): Boolean {
        val result = openSearchClient.index(
            IndexRequest.Builder<AvtaleGQL>().index(openSearchIndex)
                .id(avtale.avtaleId.toString())
                .document(avtale)
                .build()
        ).result()
        val result2 = openSearchClient.index(
            IndexRequest.Builder<AvtaleGQL>().index("${openSearchIndex}-historikk")
                .id("${avtale.avtaleId}-${avtale.versjon}")
                .document(avtale)
                .build()
        ).result()
        return result != null && result.name == "CREATED"
    }
}