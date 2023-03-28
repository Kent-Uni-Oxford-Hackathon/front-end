package uk.ac.kent.hackathon.serverservice

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.password.PasswordEncoder
import uk.ac.kent.hackathon.serverservice.config.ApplicationConfig
import uk.ac.kent.hackathon.serverservice.entities.EtherAccount
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.repository.EtherAccountRepository
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository

@SpringBootApplication
class ServerServiceApplication(
    private val passwordEncoder: PasswordEncoder,
    private val applicationConfig: ApplicationConfig
) {

    @Bean
    fun createAdminUser(userDetailsRepository: UserDetailsRepository, etherAccountRepository: EtherAccountRepository) = ApplicationRunner {
        val etherAccount = EtherAccount("A_PK_HASH")
        etherAccountRepository.save(etherAccount)
        val password = passwordEncoder.encode(applicationConfig.adminPassword)
        userDetailsRepository.save(UserDetailsImpl("admin", password, etherAccount))
    }
}

fun main(args: Array<String>) {
    runApplication<ServerServiceApplication>(*args)
}
