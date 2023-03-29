package uk.ac.kent.hackathon.serverservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ApplicationConfig {

    @Value("\${application.name}")
    lateinit var applicationName: String

    @Value("\${application.defaultPassword}")
    lateinit var defaultPassword: String
}
