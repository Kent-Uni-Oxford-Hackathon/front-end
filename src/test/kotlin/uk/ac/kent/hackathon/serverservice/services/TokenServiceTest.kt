package uk.ac.kent.hackathon.serverservice.services

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import uk.ac.kent.hackathon.serverservice.config.EtherscanConfig
import uk.ac.kent.hackathon.serverservice.entities.EtherAccount
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl

@SpringBootTest(classes = [TokenService::class, EtherscanConfig::class])
class TokenServiceTest {

    companion object {
        private const val USERNAME: String = "bTokenString"
        private const val PASSWORD: String = "aTokenString"
    }

    private lateinit var etherAccount: EtherAccount
    private lateinit var userDetailsImpl: UserDetailsImpl

    @Autowired
    lateinit var tokenService: TokenService

    @BeforeEach
    fun setUp() {
        etherAccount = EtherAccount("0x60ec0d256278c4d75dcc5bb607494ab164825cd9")
        userDetailsImpl = UserDetailsImpl(USERNAME, PASSWORD, etherAccount)
    }

    @Test
    fun getTokensByUser() {

        val tokensByUser = tokenService.getTokensByUser(userDetailsImpl)


    }
}