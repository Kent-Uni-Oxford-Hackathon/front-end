package uk.ac.kent.hackathon.serverservice.services

import org.apache.commons.lang3.RandomStringUtils.random
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.emptyCollectionOf
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder.fromHttpUrl
import uk.ac.kent.hackathon.serverservice.config.EtherscanConfig
import uk.ac.kent.hackathon.serverservice.domain.NFTResponse
import uk.ac.kent.hackathon.serverservice.domain.Token
import uk.ac.kent.hackathon.serverservice.domain.TokenNFTTxResponse
import uk.ac.kent.hackathon.serverservice.entities.EtherAccount
import uk.ac.kent.hackathon.serverservice.entities.TokenDescriptionPair
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.repository.TokenDescriptionPairRepository
import java.lang.System.currentTimeMillis
import java.net.URI
import java.util.Optional.empty
import java.util.Optional.of

@SpringBootTest(classes = [TokenService::class])
class TokenServiceTest {

    companion object {
        private const val ETHERSCAN_API_KEY = "anAPIKey"
        private const val USERNAME = "bTokenString"
        private const val PASSWORD = "aTokenString"
        private const val DESCRIPTION = "aDescription"
        private const val HEX_CHARS = "123456789abcdef"
    }

    @MockBean
    private lateinit var etherscanConfig: EtherscanConfig

    @MockBean
    private lateinit var restTemplate: RestTemplate

    @MockBean
    private lateinit var tokenDescriptionPairRepository: TokenDescriptionPairRepository

    @Autowired
    private lateinit var tokenService: TokenService

    private lateinit var etherAccount: EtherAccount
    private lateinit var userDetailsImpl: UserDetailsImpl
    private lateinit var getTransactionsEndpoint: URI

    @BeforeEach
    fun setUp() {
        etherAccount = EtherAccount("0x${random(64, HEX_CHARS)}")
        userDetailsImpl = UserDetailsImpl(USERNAME, PASSWORD, etherAccount)
        getTransactionsEndpoint = fromHttpUrl("https://api-sepolia.etherscan.io/api")
            .queryParam("module", "account")
            .queryParam("action", "tokennfttx")
            .queryParam("contractaddress", "0x05d1AD3ff7bed18e442AE9AcB34d967a1638dC71")
            .queryParam("address", etherAccount.ethPkHash)
            .queryParam("page", "1")
            .queryParam("offset", "100")
            .queryParam("startblock", "0")
            .queryParam("endblock", "99999999")
            .queryParam("sort", "asc")
            .queryParam("apikey", etherscanConfig.etherscanApiKey)
            .build().toUri()
    }


    @Test
    fun givenOwnedTokenWhenGetTokensByUserThenReturnOwnedToken() {
        val nftResponses = listOf(
            NFTResponse(
                3180580,
                currentTimeMillis(),
                "0x${random(64, HEX_CHARS)}",
                10,
                "0x${random(64, HEX_CHARS)}",
                etherAccount.ethPkHash,
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                1,
                "Geography Tokens",
                "GEO",
                0,
                7,
                1017393,
                2501295302,
                1015388,
                3711302,
                "deprecated",
                1915
            ),
            NFTResponse(
                3180580,
                currentTimeMillis(),
                "0x${random(64, HEX_CHARS)}",
                10,
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                etherAccount.ethPkHash,
                2,
                "Geography Tokens",
                "GEO",
                0,
                7,
                3,
                2,
                8,
                2,
                "deprecated",
                5
            ),
            NFTResponse(
                3180580,
                currentTimeMillis(),
                "0x${random(64, HEX_CHARS)}",
                10,
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                3,
                "Geography Tokens",
                "GEO",
                0,
                7,
                3,
                2,
                8,
                2,
                "deprecated",
                5
            )
        )

        etherscanConfig.etherscanApiKey = ETHERSCAN_API_KEY
        val expectedResponse = TokenNFTTxResponse(1, "OK", nftResponses)
        given(restTemplate.getForObject(getTransactionsEndpoint, TokenNFTTxResponse::class.java))
            .willReturn(expectedResponse)

        val tokensByUser = tokenService.getTokensByUser(userDetailsImpl)

        assertThat(tokensByUser, contains(Token(2, userDetailsImpl, "")))

        then(restTemplate).should(times(1))
            .getForObject(getTransactionsEndpoint, TokenNFTTxResponse::class.java)
        then(restTemplate).shouldHaveNoMoreInteractions()
        then(tokenDescriptionPairRepository).should(times(1)).findById(1)
        then(tokenDescriptionPairRepository).should(times(1)).findById(2)
        then(tokenDescriptionPairRepository).should(times(1)).findById(3)
        then(tokenDescriptionPairRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenSentAllTokensWhenGetTokensByUserThenReturnNoTokens() {
        val nftResponses = listOf(
            NFTResponse(
                3180580,
                currentTimeMillis(),
                "0x${random(64, HEX_CHARS)}",
                10,
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                etherAccount.ethPkHash,
                1,
                "Geography Tokens",
                "GEO",
                0,
                7,
                1017393,
                2501295302,
                1015388,
                3711302,
                "deprecated",
                1915
            ),
            NFTResponse(
                3180580,
                currentTimeMillis(),
                "0x${random(64, HEX_CHARS)}",
                10,
                "0x${random(64, HEX_CHARS)}",
                etherAccount.ethPkHash,
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                1,
                "Geography Tokens",
                "GEO",
                0,
                7,
                3,
                2,
                8,
                2,
                "deprecated",
                5
            ),
        )

        etherscanConfig.etherscanApiKey = ETHERSCAN_API_KEY
        val expectedResponse = TokenNFTTxResponse(1, "OK", nftResponses)
        given(restTemplate.getForObject(getTransactionsEndpoint, TokenNFTTxResponse::class.java))
            .willReturn(expectedResponse)
        given(tokenDescriptionPairRepository.findById(1)).willReturn(empty())

        val tokensByUser = tokenService.getTokensByUser(userDetailsImpl)

        assertThat(tokensByUser, emptyCollectionOf(Token::class.java))

        then(restTemplate).should(times(1))
            .getForObject(getTransactionsEndpoint, TokenNFTTxResponse::class.java)
        then(restTemplate).shouldHaveNoMoreInteractions()
        then(tokenDescriptionPairRepository).should(times(nftResponses.size)).findById(1)
        then(tokenDescriptionPairRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenDescriptionWhenGetTokensByUserThenReturnWithDescription() {
        val nftResponses = listOf(
            NFTResponse(
                3180580,
                currentTimeMillis(),
                "0x${random(64, HEX_CHARS)}",
                10,
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                "0x${random(64, HEX_CHARS)}",
                etherAccount.ethPkHash,
                1,
                "Geography Tokens",
                "GEO",
                0,
                7,
                1017393,
                2501295302,
                1015388,
                3711302,
                "deprecated",
                1915
            ),
        )

        etherscanConfig.etherscanApiKey = ETHERSCAN_API_KEY
        val expectedResponse = TokenNFTTxResponse(1, "OK", nftResponses)
        given(restTemplate.getForObject(getTransactionsEndpoint, TokenNFTTxResponse::class.java))
            .willReturn(expectedResponse)
        given(tokenDescriptionPairRepository.findById(1)).willReturn(of(TokenDescriptionPair(1, DESCRIPTION)))

        val tokensByUser = tokenService.getTokensByUser(userDetailsImpl)

        assertThat(tokensByUser, contains(Token(1, userDetailsImpl, DESCRIPTION)))

        then(restTemplate).should(times(1))
            .getForObject(getTransactionsEndpoint, TokenNFTTxResponse::class.java)
        then(restTemplate).shouldHaveNoMoreInteractions()
        then(tokenDescriptionPairRepository).should(times(1)).findById(1)
        then(tokenDescriptionPairRepository).shouldHaveNoMoreInteractions()
    }


}