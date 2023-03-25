package uk.ac.kent.hackathon.serverservice.entities

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.empty
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserDetailsImplTest {

    companion object {
        private const val USERNAME = "aUsername"
        private const val PASSWORD = "aPassword"
    }

    private lateinit var userDetailsImpl: UserDetailsImpl

    @BeforeEach
    fun setUp() {
        userDetailsImpl = UserDetailsImpl(USERNAME, PASSWORD)
    }

    @Test
    fun givenUserWhenGetUsernameThenReturnUsername() {
        assertThat(userDetailsImpl.username, equalTo(USERNAME))
    }

    @Test
    fun givenUserWhenGetPasswordThenReturnPassword() {
        assertThat(userDetailsImpl.password, equalTo(PASSWORD))
    }

    @Test
    fun givenUserWhenGetAuthoritiesThenReturnEmpty() {
        assertThat(userDetailsImpl.authorities, empty())
    }

    @Test
    fun givenUserWhenIsAccountNonExpiredThenReturnTrue() {
        assertThat(userDetailsImpl.isAccountNonExpired, equalTo(true))
    }

    @Test
    fun givenUserWhenIsAccountNonLockedThenReturnTrue() {
        assertThat(userDetailsImpl.isAccountNonLocked, equalTo(true))
    }

    @Test
    fun givenUserWhenIsCredentialsNonExpiredThenReturnTrue() {
        assertThat(userDetailsImpl.isCredentialsNonExpired, equalTo(true))
    }

    @Test
    fun givenUserWhenIsEnabledThenReturnTrue() {
        assertThat(userDetailsImpl.isEnabled, equalTo(true))
    }
}