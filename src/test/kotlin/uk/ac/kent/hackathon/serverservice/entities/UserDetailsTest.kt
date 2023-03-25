package uk.ac.kent.hackathon.serverservice.entities

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserDetailsTest {

    companion object {
        private const val USERNAME = "aUsername"
        private const val PASSWORD = "aPassword"
    }

    private lateinit var userDetails: UserDetails

    @BeforeEach
    fun setUp() {
        userDetails = UserDetails(USERNAME, PASSWORD)
    }

    @Test
    fun givenUserWhenGetUsernameThenReturnUsername() {
        assertThat(userDetails.username, equalTo(USERNAME))
    }

    @Test
    fun givenUserWhenGetPasswordThenReturnPassword() {
        assertThat(userDetails.password, equalTo(PASSWORD))
    }

    @Test
    fun givenUserWhenGetAuthoritiesThenReturnEmpty() {
        assertThat(userDetails.authorities, empty())
    }

    @Test
    fun givenUserWhenIsAccountNonExpiredThenReturnTrue() {
        assertThat(userDetails.isAccountNonExpired, equalTo(true))
    }

    @Test
    fun givenUserWhenIsAccountNonLockedThenReturnTrue() {
        assertThat(userDetails.isAccountNonLocked, equalTo(true))
    }

    @Test
    fun givenUserWhenIsCredentialsNonExpiredThenReturnTrue() {
        assertThat(userDetails.isCredentialsNonExpired, equalTo(true))
    }

    @Test
    fun givenUserWhenIsEnabledThenReturnTrue() {
        assertThat(userDetails.isEnabled, equalTo(true))
    }
}