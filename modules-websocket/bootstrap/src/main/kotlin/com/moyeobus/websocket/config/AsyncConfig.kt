package com.moyeobus.websocket.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class AsyncConfig {
    @Bean
    fun taskExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 10       // 기본 스레드 수
        executor.maxPoolSize = 50        // 최대 스레드 수
        executor.queueCapacity = 100     // 대기 큐 크기
        executor.setThreadNamePrefix("async-")
        executor.initialize()
        return executor
    }
}