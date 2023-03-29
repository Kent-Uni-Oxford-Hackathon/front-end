package uk.ac.kent.hackathon.serverservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import uk.ac.kent.hackathon.serverservice.domain.Contract

@Configuration
class ContractConfig {
    @Value("\${contract.categories}")
    lateinit var categories: Array<String>

    @Value("\${contract.imagePaths}")
    lateinit var imagePaths: Array<String>

    @Value("\${contract.addresses}")
    lateinit var addresses: Array<String>

    fun getCategoriesToAddressPairs(): List<Contract> {
        return categories.foldIndexed(mutableListOf()) { index, acc, category -> acc.apply { add(Contract(category, addresses[index], imagePaths[index])) } }
    }
}
