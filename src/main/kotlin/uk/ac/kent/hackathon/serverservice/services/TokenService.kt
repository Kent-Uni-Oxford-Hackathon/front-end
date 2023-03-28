package uk.ac.kent.hackathon.serverservice.services

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.util.UriComponentsBuilder
import uk.ac.kent.hackathon.serverservice.config.EtherscanConfig
import uk.ac.kent.hackathon.serverservice.domain.Token
import uk.ac.kent.hackathon.serverservice.domain.TokenNFTTxResponse
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl

@Service
class TokenService(private val etherscanConfig: EtherscanConfig, private val restTemplate: RestTemplate) {

    fun getTokensByUser(userDetailsImpl: UserDetailsImpl): Collection<Token> {
        val uri = UriComponentsBuilder.fromHttpUrl("https://api-sepolia.etherscan.io/api")
            .queryParam("module", "account")
            .queryParam("action", "tokennfttx")
            .queryParam("contractaddress", "0x05d1AD3ff7bed18e442AE9AcB34d967a1638dC71")
            .queryParam("address", userDetailsImpl.etherAccount.ethPkHash)
            .queryParam("page", "1")
            .queryParam("offset", "100")
            .queryParam("startblock", "0")
            .queryParam("endblock", "99999999")
            .queryParam("sort", "asc")
            .queryParam("apikey", etherscanConfig.etherscanApiKey)
            .build().toUri()

        val newList = mutableListOf<Token>()
        restTemplate.getForObject<TokenNFTTxResponse>(uri).result.forEach {
            val token = Token(it.tokenId, userDetailsImpl, "") // TODO: Fix description lol
            if (it.to == userDetailsImpl.etherAccount.ethPkHash) {
                newList.add(token)
            } else if (it.from == userDetailsImpl.etherAccount.ethPkHash) {
                newList.remove(token)
            }
        }

        return newList
    }
}