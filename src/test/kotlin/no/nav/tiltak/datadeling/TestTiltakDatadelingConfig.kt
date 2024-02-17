package no.nav.tiltak.datadeling

import com.fasterxml.jackson.databind.ObjectMapper
import no.nav.tiltak.datadeling.opensearch.OpenSearchProperties
import no.nav.tiltak.datadeling.opensearch.buildOpenSearchClient
import org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.CommonClientConfigs.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.*
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.opensearch.client.opensearch.OpenSearchClient
import org.opensearch.testcontainers.OpensearchContainer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName


@Profile("!dockercompose")
@TestConfiguration(proxyBeanMethods = false)
class TestTiltakDatadelingConfig {

    @Bean
    @ServiceConnection
    fun kafkaContainer(): KafkaContainer {
        return KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"))
    }

    @Bean
    //@ServiceConnection
    fun opensearchContainer(): OpensearchContainer {
        return OpensearchContainer(DockerImageName.parse("opensearchproject/opensearch:2.0.0"))
    }

    @Bean
    fun openSearchClient(opensearchContainer: OpensearchContainer, mapper: ObjectMapper): OpenSearchClient {
        return buildOpenSearchClient(
            OpenSearchProperties(
                opensearchContainer.httpHostAddress,
                opensearchContainer.username,
                opensearchContainer.password
            ),
            mapper
        )
    }

    @Bean
    fun kafkaConsumer(kafkaContainer: KafkaContainer): KafkaConsumer<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[BOOTSTRAP_SERVERS_CONFIG] = kafkaContainer.bootstrapServers
        props[AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props[GROUP_ID_CONFIG] = "tiltak"
        props[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return KafkaConsumer<String, String>(props)
    }

    @Bean
    fun kafkaConsumerFactory(kafkaContainer: KafkaContainer): ConsumerFactory<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[BOOTSTRAP_SERVERS_CONFIG] = kafkaContainer.bootstrapServers
        props[AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props[GROUP_ID_CONFIG] = "tiltak"
        props[KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }
    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        return factory
    }

    @Bean
    fun kafkaProducer(kafkaContainer: KafkaContainer): KafkaProducer<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[BOOTSTRAP_SERVERS_CONFIG] = kafkaContainer.bootstrapServers
        props[KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        return KafkaProducer<String, String>(props)
    }
}
