package com.moyeobus.scheduler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.moyeobus.scheduler"])
class MoyeoBusSchedulerApplication
fun main(args: Array<String>) {
	runApplication<MoyeoBusSchedulerApplication>(*args)
}
