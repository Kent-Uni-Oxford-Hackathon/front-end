package uk.ac.kent.hackathon.serverservice.services

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import uk.ac.kent.hackathon.serverservice.entities.EtherAccount
import uk.ac.kent.hackathon.serverservice.entities.Token
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.repository.TokenRepository
import java.lang.System.currentTimeMillis
import java.sql.Timestamp

@SpringBootTest(classes = [TokenService::class])
class TokenServiceTest {

    @Autowired
    lateinit var tokenService: TokenService

    @MockBean
    lateinit var tokenRepository: TokenRepository

    companion object {
        private const val TOKEN_ID: String = "aTokenId"
        private const val TOKEN_STRING: String = "aTokenString"
        private const val USERNAME_A: String = "aTokenString"
        private const val USERNAME_B: String = "bTokenString"
        private const val PASSWORD: String = "aTokenString"
        private const val DESCRIPTION: String = "aTokenString"
        private const val ETH_PK_HASH = "aPkHash"
        private val CATEGORIES: Collection<String> = listOf("art", "science")
        private val TIMESTAMP: Timestamp = Timestamp(currentTimeMillis())
        private val ETHER_ACCOUNT = EtherAccount(ETH_PK_HASH)
        private val USER_A: UserDetailsImpl = UserDetailsImpl(USERNAME_A, PASSWORD, ETHER_ACCOUNT)
        private val USER_B: UserDetailsImpl = UserDetailsImpl(USERNAME_B, PASSWORD, ETHER_ACCOUNT)
        private val TOKEN = Token(TOKEN_ID, TOKEN_STRING, USER_A, CATEGORIES, TIMESTAMP, DESCRIPTION)
    }

    @Test
    fun givenTokenWhenTransferTokenThenSaveUpdatedToken() {
        val newToken = Token(TOKEN_ID, TOKEN_STRING, USER_B, CATEGORIES, TIMESTAMP, DESCRIPTION)
        given(tokenRepository.save(any())).willReturn(newToken)

        tokenService.transferToken(TOKEN, USER_B)

        then(tokenRepository).should(times(1)).save(newToken)
        then(tokenRepository).shouldHaveNoMoreInteractions()
    }
}