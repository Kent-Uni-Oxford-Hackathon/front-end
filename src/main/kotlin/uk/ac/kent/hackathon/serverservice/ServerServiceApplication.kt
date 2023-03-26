package uk.ac.kent.hackathon.serverservice

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository

@SpringBootApplication
class ServerServiceApplication {

    @Bean
    fun commandLineRunner(userDetailsRepository: UserDetailsRepository): CommandLineRunner {
        return CommandLineRunner { userDetailsRepository.save(UserDetailsImpl("admin", "{noop}password")) }
    }
}

fun main(args: Array<String>) {
    runApplication<ServerServiceApplication>(*args)
}
