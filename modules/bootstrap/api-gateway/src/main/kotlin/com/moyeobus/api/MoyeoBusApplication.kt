package com.moyeobus.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.moyeobus"])
class MoyeoBusApplication

fun main(args: Array<String>) {
	runApplication<MoyeoBusApplication>(*args)
}
