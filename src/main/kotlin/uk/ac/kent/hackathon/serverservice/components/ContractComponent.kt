package uk.ac.kent.hackathon.serverservice.components

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import uk.ac.kent.hackathon.serverservice.config.ContractConfig
import uk.ac.kent.hackathon.serverservice.domain.Contract

@Component
class ContractComponent(private val contractConfig: ContractConfig) {

    @Bean
    fun contracts() = contractConfig.categories.foldIndexed(mutableListOf<Contract>()) { index, acc, category ->
        val name = category.split("-")
        acc.apply { add(Contract(name[1], name[0], contractConfig.addresses[index], contractConfig.imagePaths[index])) }
    }

    @Bean
    fun groups() = contracts().fold(mutableSetOf<String>()) {
        acc, contract -> acc.apply { acc.add(contract.group) }
    }
}