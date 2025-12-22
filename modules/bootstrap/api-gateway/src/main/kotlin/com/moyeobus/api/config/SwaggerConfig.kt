package com.moyeobus.api.config

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
            .title("MoyeoBus API")
            .version("v1.0")
            .description(
                """
                  - ‼️`서버 DNS 변경 - https://api.moyeobus.com/api/v1` ‼️ </br></br>
                  - ‼️`로그아웃, 토큰 재발급 API 추가`‼️ </br></br>
                  - `✅ = API 변동이 생겼으니 수정 필요` </br></br>
                  - `로그인 하면 해당 스웨거 페이지에 자동으로 쿠키가 생기니까 쿠키 비어있으면 로그인하면 돼.`
                """.trimIndent()
            )
}