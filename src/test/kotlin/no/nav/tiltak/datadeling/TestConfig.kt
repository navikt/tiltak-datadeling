package no.nav.tiltak.datadeling

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker

@Configuration
class TestConfig {
    @Bean
    fun kraftBroker(@Value("\${tiltak-datadeling.kafka.topic}") topic: String): EmbeddedKafkaKraftBroker {
        return EmbeddedKafkaKraftBroker(1, 1, topic)
    }

    @Bean
    fun kafkaConsumer(kraftBroker: EmbeddedKafkaKraftBroker): KafkaConsumer<String, String> {
        return KafkaConsumer(
            mapOf(
                "bootstrap.servers" to kraftBroker.brokersAsString,
                "group.id" to "test-group",
                "enable.auto.commit" to "false",
                "key.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
                "value.deserializer" to "org.apache.kafka.common.serialization.StringDeserializer",
            )
        )
    }

    @Bean
    fun kafkaProducer(kraftBroker: EmbeddedKafkaKraftBroker): KafkaProducer<String, String> {
        return KafkaProducer(
            mapOf(
                "bootstrap.servers" to kraftBroker.brokersAsString,
                "key.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer" to "org.apache.kafka.common.serialization.StringSerializer",
            )
        )
    }

    @Bean
    fun kafkaConsumerFactory(kraftBroker: EmbeddedKafkaKraftBroker): ConsumerFactory<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        props[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = kraftBroker.brokersAsString
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
        props[CommonClientConfigs.GROUP_ID_CONFIG] = "tiltak"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, String>): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        return factory
    }
}
