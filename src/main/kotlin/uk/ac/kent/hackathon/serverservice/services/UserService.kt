package uk.ac.kent.hackathon.serverservice.services

import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository

@Service
class UserService(private val userDetailsRepository: UserDetailsRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String) =
        userDetailsRepository
            .findById(username)
            .orElseThrow { UsernameNotFoundException("Username '${username}' not found!") }!!

}