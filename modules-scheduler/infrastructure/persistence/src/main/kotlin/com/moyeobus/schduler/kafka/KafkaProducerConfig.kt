package com.moyeobus.schduler.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
class KafkaProducerConfig(

    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapServers: String
) {

    fun producerConfig(): Map<String, Any> =
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,

            JsonSerializer.ADD_TYPE_INFO_HEADERS to false,      // class 정보 제거
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true,   // 중복 방지
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.RETRIES_CONFIG to 10,
            ProducerConfig.COMPRESSION_TYPE_CONFIG to "lz4",
            ProducerConfig.LINGER_MS_CONFIG to 10,
            ProducerConfig.BATCH_SIZE_CONFIG to 16384
        )


    @Bean
    fun producerFactory(): ProducerFactory<String, Any> =
        DefaultKafkaProducerFactory(producerConfig())


    @Bean
    fun kafkaTemplate(
        producerFactory: ProducerFactory<String, Any>
    ): KafkaTemplate<String, Any> =
        KafkaTemplate(producerFactory)
}