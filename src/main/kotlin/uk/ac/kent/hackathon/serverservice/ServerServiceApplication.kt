package uk.ac.kent.hackathon.serverservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerServiceApplication

fun main(args: Array<String>) {
	runApplication<ServerServiceApplication>(*args)
}
