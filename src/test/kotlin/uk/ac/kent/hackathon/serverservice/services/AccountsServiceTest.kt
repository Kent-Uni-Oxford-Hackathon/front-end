package uk.ac.kent.hackathon.serverservice.services

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.TestSecurityContextHolder
import uk.ac.kent.hackathon.serverservice.entities.EtherAccount
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.exception.UsernameAlreadyExistsException
import uk.ac.kent.hackathon.serverservice.repository.EtherAccountRepository
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository
import java.util.Optional.empty
import java.util.Optional.of

@SpringBootTest(classes = [AccountsService::class])
class AccountsServiceTest {

    companion object {
        private const val USERNAME = "aUsername"
        private const val PASSWORD = "aPassword"
        private const val ETH_PK_HASH = "A_ETH_PK"
    }

    private lateinit var etherAccount: EtherAccount
    private lateinit var userDetails: UserDetailsImpl

    @BeforeEach
    fun setUp() {
        etherAccount = EtherAccount(ETH_PK_HASH)
        userDetails = UserDetailsImpl(USERNAME, PASSWORD, etherAccount)
    }

    @Autowired
    private lateinit var accountsService: AccountsService

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @MockBean
    private lateinit var userRepository: UserDetailsRepository

    @MockBean
    private lateinit var etherAccountRepository: EtherAccountRepository

    @Test
    fun givenUserExistsWhenLoadUserByUsernameThenReturnUser() {
        given(userRepository.findById(USERNAME)).willReturn(of(userDetails))

        val user = accountsService.loadUserByUsername(USERNAME)

        assertThat(user, equalTo(userDetails))
        then(userRepository).should(times(1)).findById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserExistsWhenLoadUserByUsernameThenThrow() {
        given(userRepository.existsById(USERNAME)).willReturn(false)

        try {
            accountsService.loadUserByUsername(USERNAME)
        } catch (e: UsernameNotFoundException) {
            assertThat(e.message, equalTo("Username '${USERNAME}' not found!"))
        }

        then(userRepository).should(times(1)).findById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserAlreadyExistsThenCreateUserThenSave() {
        val encodedNewPassword = "encryptedNewPassword"
        given(passwordEncoder.encode(PASSWORD)).willReturn(encodedNewPassword)
        given(userRepository.existsById(USERNAME)).willReturn(false)

        accountsService.createUser(userDetails)

        then(etherAccountRepository).should(times(1)).save(etherAccount)
        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(times(1)).save(UserDetailsImpl(USERNAME, encodedNewPassword, etherAccount))
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserAlreadyExistsWhenCreateUserThenSave() {
        given(userRepository.existsById(USERNAME)).willReturn(true)

        try {
            accountsService.createUser(userDetails)
        } catch (e: UsernameAlreadyExistsException) {
            assertThat(e.message, equalTo("User with username '${USERNAME}' already exists!"))
        }

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(never()).save(userDetails)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserWhenDeleteUserThenDelete() {
        given(userRepository.existsById(USERNAME)).willReturn(true)

        accountsService.deleteUser(USERNAME)

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(times(1)).deleteById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserWhenDeleteUserThenThrow() {
        given(userRepository.existsById(USERNAME)).willReturn(false)

        try {
            accountsService.deleteUser(USERNAME)
        } catch (e: UsernameNotFoundException) {
            assertThat(e.message, equalTo("User with username '${USERNAME}' not found!"))
        }

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(never()).delete(userDetails)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserWhenChangePasswordThenSave() {
        val newPassword = "newPassword"
        val encodedNewPassword = "encryptedNewPassword"
        given(userRepository.findById(USERNAME)).willReturn(of(userDetails))
        given(passwordEncoder.encode(newPassword)).willReturn(encodedNewPassword)
        TestSecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)

        accountsService.changePassword(PASSWORD, newPassword)

        then(userRepository).should(times(1)).findById(USERNAME)
        then(userRepository).should(times(1)).save(UserDetailsImpl(USERNAME, encodedNewPassword, etherAccount))
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserWhenChangePasswordThenThrow() {
        given(userRepository.findById(USERNAME)).willReturn(empty())

        val user = UserDetailsImpl(USERNAME, "newPassword", etherAccount)

        try {
            accountsService.updateUser(user)
        } catch (e: UsernameNotFoundException) {
            assertThat(e.message, equalTo("User with username '${USERNAME}' not found!"))
        }

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(never()).save(any())
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserWhenUserExistsThenReturnTrue() {
        given(userRepository.existsById(USERNAME)).willReturn(true)

        val userExists = accountsService.userExists(USERNAME)

        assertThat(userExists, equalTo(true))
        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserWhenUserExistsThenReturnFalse() {
        given(userRepository.existsById(USERNAME)).willReturn(false)

        val userExists = accountsService.userExists(USERNAME)

        assertThat(userExists, equalTo(false))
        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
        then(etherAccountRepository).shouldHaveNoMoreInteractions()
    }
}