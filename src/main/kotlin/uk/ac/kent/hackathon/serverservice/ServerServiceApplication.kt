package uk.ac.kent.hackathon.serverservice

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
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
    fun createDefaultUsers(userDetailsRepository: UserDetailsRepository, etherAccountRepository: EtherAccountRepository) = ApplicationRunner {
        val etherAccount = EtherAccount("A_PK_HASH")
        val joEtherAccount = EtherAccount("0x60ec0d256278c4d75dcc5bb607494ab164825cd9")
        val demoEtherAccount = EtherAccount("0xd05b7dc35264a651cf0baf51b9f26adcf103c824")
        etherAccountRepository.saveAll(listOf(etherAccount, joEtherAccount, demoEtherAccount))
        val password = passwordEncoder.encode(applicationConfig.defaultPassword)
        userDetailsRepository.save(UserDetailsImpl("admin", password, etherAccount))
        userDetailsRepository.save(UserDetailsImpl("demo", password, demoEtherAccount))
        userDetailsRepository.save(UserDetailsImpl("jo", password, joEtherAccount))
    }

    @Bean
    fun restTemplate() = RestTemplateBuilder().build()!!
}

fun main(args: Array<String>) {
    runApplication<ServerServiceApplication>(*args)
}
