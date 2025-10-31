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
}