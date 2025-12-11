package com.moyeobus.infra.external.kafka.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String
) {

    private fun baseConsumerConfigs(): Map<String, Any> =
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            JsonDeserializer.USE_TYPE_INFO_HEADERS to false
        )

    private fun <T : Any> createConsumerFactory(clazz: Class<T>): ConsumerFactory<String, T> =
        DefaultKafkaConsumerFactory(
            baseConsumerConfigs(),
            StringDeserializer(),
            JsonDeserializer(clazz, false)
        )

    private fun <T : Any> createListenerContainerFactory(
        factory: ConsumerFactory<String, T>
    ): ConcurrentKafkaListenerContainerFactory<String, T> =
        ConcurrentKafkaListenerContainerFactory<String, T>().apply {
            setConsumerFactory(factory)
        }

}