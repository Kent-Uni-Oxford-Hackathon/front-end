package uk.ac.kent.hackathon.serverservice.services

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.TestSecurityContextHolder
import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl
import uk.ac.kent.hackathon.serverservice.exception.UserAlreadyExistsException
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository
import java.util.Optional.empty
import java.util.Optional.of

@SpringBootTest(classes = [UserDetailsService::class])
class UserDetailsServiceTest {

    companion object {
        private const val USERNAME = "aUsername"
        private const val PASSWORD = "aPassword"
        private val USER_DETAILS = UserDetailsImpl(USERNAME, PASSWORD)
    }

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @MockBean
    private lateinit var userRepository: UserDetailsRepository

    @Test
    fun givenUserExistsWhenLoadUserByUsernameThenReturnUser() {
        given(userRepository.findById(USERNAME)).willReturn(of(USER_DETAILS))

        val user = userDetailsService.loadUserByUsername(USERNAME)

        assertThat(user, equalTo(USER_DETAILS))
        then(userRepository).should(times(1)).findById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserExistsWhenLoadUserByUsernameThenThrow() {
        given(userRepository.existsById(USERNAME)).willReturn(false)

        try {
            userDetailsService.loadUserByUsername(USERNAME)
        } catch (e: UsernameNotFoundException) {
            assertThat(e.message, equalTo("Username '${USERNAME}' not found!"))
        }

        then(userRepository).should(times(1)).findById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserAlreadyExistsThenCreateUserThenSave() {
        given(userRepository.existsById(USERNAME)).willReturn(false)

        userDetailsService.createUser(USER_DETAILS)

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(times(1)).save(USER_DETAILS)
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserAlreadyExistsWhenCreateUserThenSave() {
        given(userRepository.existsById(USERNAME)).willReturn(true)

        try {
            userDetailsService.createUser(USER_DETAILS)
        } catch (e: UserAlreadyExistsException) {
            assertThat(e.message, equalTo("User with username '${USERNAME}' already exists!"))
        }

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(never()).save(USER_DETAILS)
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserWhenDeleteUserThenDelete() {
        given(userRepository.existsById(USERNAME)).willReturn(true)

        userDetailsService.deleteUser(USERNAME)

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(times(1)).deleteById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserWhenDeleteUserThenThrow() {
        given(userRepository.existsById(USERNAME)).willReturn(false)

        try {
            userDetailsService.deleteUser(USERNAME)
        } catch (e: UsernameNotFoundException) {
            assertThat(e.message, equalTo("User with username '${USERNAME}' not found!"))
        }

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(never()).delete(USER_DETAILS)
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserWhenChangePasswordThenSave() {
        val newPassword = "newPassword"
        val encodedNewPassword = "encryptedNewPassword"
        given(userRepository.findById(USERNAME)).willReturn(of(USER_DETAILS))
        given(passwordEncoder.encode(newPassword)).willReturn(encodedNewPassword)
        TestSecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)

        userDetailsService.changePassword(PASSWORD, newPassword)

        then(userRepository).should(times(1)).findById(USERNAME)
        then(userRepository).should(times(1)).save(UserDetailsImpl(USERNAME, encodedNewPassword))
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserWhenChangePasswordThenThrow() {
        given(userRepository.findById(USERNAME)).willReturn(empty())

        val user = UserDetailsImpl(USERNAME, "newPassword")

        try {
            userDetailsService.updateUser(user)
        } catch (e: UsernameNotFoundException) {
            assertThat(e.message, equalTo("User with username '${USERNAME}' not found!"))
        }

        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).should(never()).save(any())
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenUserWhenUserExistsThenReturnTrue() {
        given(userRepository.existsById(USERNAME)).willReturn(true)

        val userExists = userDetailsService.userExists(USERNAME)

        assertThat(userExists, equalTo(true))
        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
    }

    @Test
    fun givenNoUserWhenUserExistsThenReturnFalse() {
        given(userRepository.existsById(USERNAME)).willReturn(false)

        val userExists = userDetailsService.userExists(USERNAME)

        assertThat(userExists, equalTo(false))
        then(userRepository).should(times(1)).existsById(USERNAME)
        then(userRepository).shouldHaveNoMoreInteractions()
    }
}