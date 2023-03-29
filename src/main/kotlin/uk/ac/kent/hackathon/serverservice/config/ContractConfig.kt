package uk.ac.kent.hackathon.serverservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ContractConfig {
    @Value("\${contract.address}")
    lateinit var address: String
}
