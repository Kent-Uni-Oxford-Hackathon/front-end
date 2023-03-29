package uk.ac.kent.hackathon.serverservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class ContractConfig {
    @Value("\${contract.names}")
    lateinit var names: Array<String>

    @Value("\${contract.addresses}")
    lateinit var address: Array<String>

    fun getCategoriesToAddressPairs(): List<Pair<String, String>> {
        return names.foldIndexed(mutableListOf()) { index, acc, s -> acc.apply { add(s to address[index]) } }
    }
}
