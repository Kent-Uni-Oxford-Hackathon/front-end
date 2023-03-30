package uk.ac.kent.hackathon.serverservice.components

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import uk.ac.kent.hackathon.serverservice.config.ContractConfig
import uk.ac.kent.hackathon.serverservice.domain.Contract

@Component
class ContractComponent(private val contractConfig: ContractConfig) {

    @Bean
    fun contracts() = contractConfig.categories.foldIndexed(mutableListOf<Contract>()) { index, acc, category ->
        acc.apply { add(Contract(category, contractConfig.addresses[index], contractConfig.imagePaths[index])) }
    }
}