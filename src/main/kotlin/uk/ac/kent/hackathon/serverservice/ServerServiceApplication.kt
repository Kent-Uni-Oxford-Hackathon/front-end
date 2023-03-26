package uk.ac.kent.hackathon.serverservice

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder
import uk.ac.kent.hackathon.serverservice.config.ApplicationConfig
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository

@SpringBootApplication
class ServerServiceApplication(
    private val passwordEncoder: PasswordEncoder,
    private val applicationConfig: ApplicationConfig
) {

    @Bean
    fun commandLineRunner(userDetailsRepository: UserDetailsRepository): CommandLineRunner {
        return CommandLineRunner {
            userDetailsRepository.save(
                UserDetailsImpl(
                    "admin",
                    passwordEncoder.encode(applicationConfig.adminPassword)
                )
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<ServerServiceApplication>(*args)
}
