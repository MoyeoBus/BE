package com.moyeobus.scheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication(scanBasePackages = ["com.moyeobus.scheduler"])
@EnableScheduling
class MoyeoBusSchedulerApplication
fun main(args: Array<String>) {
	runApplication<MoyeoBusSchedulerApplication>(*args)
}
