package com.moyeobus.websocket.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {

    // registerStompEndpoints : Websocket 연결을 위한 엔드포인트를 지정한다.
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/navi-dashboard", "/bus-location").setAllowedOrigins("http://localhost:63342").withSockJS()
    }

    // setApplicationDestinationPrefixes : 서버가 목적지 일때(Client -> Server 메시지 전송시 Endpoint)
    //enableSimpleBroker : 클라이언트가 Subscribe 할떄(Server -> Client 메시지 전송 시 Endpoint)
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.setApplicationDestinationPrefixes("/app")
        registry.enableSimpleBroker("/topic")
    }
}