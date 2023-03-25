package uk.ac.kent.hackathon.serverservice.services

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.userdetails.UsernameNotFoundException
import uk.ac.kent.hackathon.serverservice.entities.UserDetails
import uk.ac.kent.hackathon.serverservice.repository.UserDetailsRepository
import java.util.Optional.empty
import java.util.Optional.of

@SpringBootTest(classes = [UserService::class])
class UserServiceTest {

    companion object {
        private const val USERNAME = "aUsername"
        private const val PASSWORD = "aPassword"
        private val USERDetails = UserDetails(USERNAME, PASSWORD)
    }

    @Autowired
    private lateinit var userService: UserService

    @MockBean
    private lateinit var userRepository: UserDetailsRepository

    @Test
    fun givenUserExistsWhenLoadUserByUsernameThenReturnUser() {
        given(userRepository.findById(USERNAME)).willReturn(of(USERDetails))

        val user = userService.loadUserByUsername(USERNAME)

        assertThat(user, equalTo(USERDetails))
        then(userRepository).should(times(1)).findById(USERNAME)
    }

    @Test
    fun givenNoUserExistsWhenLoadUserByUsernameThenThrow() {
        given(userRepository.findById(USERNAME)).willReturn(empty())

        try {
            userService.loadUserByUsername(USERNAME)
        } catch (e: UsernameNotFoundException) {
            assertThat(e.message, equalTo("Username '${USERNAME}' not found!"))
        }

        then(userRepository).should(times(1)).findById(USERNAME)
    }
}