package com.moyeobus.infra.external.kafka.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class KafkaConsumerService{
    @KafkaListener(topics = ["route-created"], groupId = "route-created",)
    fun test(){
        println("ROUTE_CREATED")
    }
}