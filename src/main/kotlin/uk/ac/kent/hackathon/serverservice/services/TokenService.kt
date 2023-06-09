package uk.ac.kent.hackathon.serverservice.services

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject
import org.springframework.web.util.UriComponentsBuilder.fromHttpUrl
import uk.ac.kent.hackathon.serverservice.config.EtherscanConfig
import uk.ac.kent.hackathon.serverservice.domain.Contract
import uk.ac.kent.hackathon.serverservice.domain.NFTResponse
import uk.ac.kent.hackathon.serverservice.domain.Token
import uk.ac.kent.hackathon.serverservice.domain.TokenNFTTxResponse
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl

@Service
class TokenService(
    private val etherscanConfig: EtherscanConfig,
    private val restTemplate: RestTemplate
) {

    fun getTokensByUserAndCategory(userDetailsImpl: UserDetailsImpl, contract: Contract) =
        restTemplate
            .getForObject<TokenNFTTxResponse>(uriComponents(userDetailsImpl, contract.address).toUri()).result
            .fold<NFTResponse, MutableList<Token>>(mutableListOf()) { acc, nftResponse ->
                val token = Token(nftResponse.tokenId, userDetailsImpl, contract)
                acc.apply {
                    when (userDetailsImpl.etherAccount.ethPkHash.lowercase()) {
                        nftResponse.to.lowercase() -> add(token)
                        nftResponse.from.lowercase() -> remove(token)
                    }
                }
            }

    private fun uriComponents(userDetailsImpl: UserDetailsImpl, contractAddress: String) =
        fromHttpUrl("https://api-sepolia.etherscan.io/api")
            .queryParam("module", "account")
            .queryParam("action", "tokennfttx")
            .queryParam("contractaddress", contractAddress)
            .queryParam("address", userDetailsImpl.etherAccount.ethPkHash)
            .queryParam("page", "1")
            .queryParam("offset", "100")
            .queryParam("startblock", "0")
            .queryParam("endblock", "99999999")
            .queryParam("sort", "asc")
            .queryParam("apikey", etherscanConfig.etherscanApiKey)
            .build()
}