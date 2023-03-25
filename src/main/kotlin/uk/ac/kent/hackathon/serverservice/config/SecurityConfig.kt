package uk.ac.kent.hackathon.serverservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder() = DelegatingPasswordEncoder("bcrypt", mapOf("bcrypt" to BCryptPasswordEncoder()))

}