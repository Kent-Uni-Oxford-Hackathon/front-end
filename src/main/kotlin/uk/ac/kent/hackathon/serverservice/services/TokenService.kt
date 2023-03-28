package uk.ac.kent.hackathon.serverservice.services

import org.springframework.stereotype.Service
import uk.ac.kent.hackathon.serverservice.entities.Token
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.repository.TokenRepository

@Service
class TokenService(private val tokenRepository: TokenRepository) {
    fun transferToken(token: Token, to: UserDetailsImpl) = tokenRepository.save(token.apply { owner = to })
}