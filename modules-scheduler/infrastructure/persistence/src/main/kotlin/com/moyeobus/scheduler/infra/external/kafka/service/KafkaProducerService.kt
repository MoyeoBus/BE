package com.moyeobus.scheduler.infra.external.kafka.service

import com.moyeobus.scheduler.application.event.EventOutport
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(
    private val kafkaTemplate: KafkaTemplate<String, Long>,
) : EventOutport {
    override fun sendEvent(topic: String, routeId: Long){
        kafkaTemplate.send(topic, routeId)
    }
}