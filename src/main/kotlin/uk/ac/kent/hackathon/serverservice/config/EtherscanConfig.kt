package uk.ac.kent.hackathon.serverservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class EtherscanConfig {
    @Value("\${etherscan.api.key}")
    lateinit var etherscanApiKey: String
}