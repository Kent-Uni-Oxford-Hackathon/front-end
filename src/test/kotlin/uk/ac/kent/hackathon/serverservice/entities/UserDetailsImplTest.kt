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
        private const val ETH_PK_HASH = "aPkHash"
    }

    private lateinit var userDetailsImpl: UserDetailsImpl
    private lateinit var etherAccount: EtherAccount

    @BeforeEach
    fun setUp() {
        etherAccount = EtherAccount(ETH_PK_HASH)
        userDetailsImpl = UserDetailsImpl(USERNAME, PASSWORD, etherAccount)
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