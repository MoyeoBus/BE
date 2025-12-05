package com.moyeobus.scheduler.infra.external.kakao

import com.moyeobus.scheduler.application.route.dto.KakaoDirectionRequest
import com.moyeobus.scheduler.application.route.dto.KakaoDirectionResponse
import com.moyeobus.scheduler.application.route.port.out.KakaoMobilityOutPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class KakaoMobilityClient(
    @param:Value("\${kakao.rest-api-key}") private val restApiKey: String,
) : KakaoMobilityOutPort {
    private val webClient: WebClient = WebClient.builder()
        .baseUrl("https://apis-navi.kakaomobility.com")
        .defaultHeader("Content-Type", "application/json")
        .defaultHeader("Authorization", "KakaoAK $restApiKey") // ← 카카오 인증 방식
        .build()

    override fun getDirections(request: KakaoDirectionRequest): KakaoDirectionResponse {
        return webClient.post()
            .uri("/v1/waypoints/directions")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(KakaoDirectionResponse::class.java)
            .block()!!
    }

}