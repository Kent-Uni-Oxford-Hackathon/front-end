package uk.ac.kent.hackathon.serverservice.services

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Service
import uk.ac.kent.hackathon.serverservice.entities.EtherAccount
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.exception.UsernameAlreadyExistsException
import uk.ac.kent.hackathon.serverservice.repository.EtherAccountRepository
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository

@Service
class AccountsService(
    private val etherAccountRepository: EtherAccountRepository,
    private val userDetailsRepository: UserDetailsRepository,
    private val passwordEncoder: PasswordEncoder,
) : UserDetailsManager {

    override fun loadUserByUsername(username: String) =
        userDetailsRepository
            .findById(username)
            .orElseThrow { UsernameNotFoundException("Username '${username}' not found!") }!!

    override fun createUser(user: UserDetails) {
        user as UserDetailsImpl
        if (userExists(user.username)) throw UsernameAlreadyExistsException("User with username '${user.username}' already exists!")
        user
            .apply { user.password = passwordEncoder.encode(user.password) }
            .also(userDetailsRepository::save)
    }

    fun createUser(username: String, password: String) {
        val etherAccount = etherAccountRepository.save(generateEtherAccount())
        createUser(UserDetailsImpl(username, password, etherAccount))
    }

    private fun generateEtherAccount() = EtherAccount("A_ETH_PK") // TODO: Pull from API

    override fun updateUser(user: UserDetails) {
        if (!userExists(user.username)) throw UsernameNotFoundException("User with username '${user.username}' not found!")
        userDetailsRepository.save(user as UserDetailsImpl)
    }

    override fun deleteUser(username: String) {
        if (!userExists(username)) throw UsernameNotFoundException("User with username '${username}' not found!")
        userDetailsRepository.deleteById(username)
    }

    override fun changePassword(oldPassword: String, newPassword: String) {
        with(SecurityContextHolder.getContext().authentication.name) {
            userDetailsRepository.findByIdOrNull(this)
                ?.apply { password = passwordEncoder.encode(newPassword) }
                ?.also(userDetailsRepository::save)
                ?: throw UsernameNotFoundException("User with username '${this}' not found!")
        }
    }

    override fun userExists(username: String) = userDetailsRepository.existsById(username)

}