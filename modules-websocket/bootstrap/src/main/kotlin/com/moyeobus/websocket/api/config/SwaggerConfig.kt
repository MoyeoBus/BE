package com.moyeobus.websocket.api.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .openapi("3.0.1")
            .addServersItem(Server().url("/"))
            .info(apiInfo())
    }

    private fun apiInfo(): Info =
        Info()
            .title("MoyeoBus SSE API")
            .version("v1.0")
            .description(
                """
                  실시간 통신 기능에 필요한 API를 모아놓은 서버 입니다.
                """.trimIndent()
            )
}