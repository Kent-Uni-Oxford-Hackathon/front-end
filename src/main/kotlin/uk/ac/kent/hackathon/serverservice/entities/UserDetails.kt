package uk.ac.kent.hackathon.serverservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class UserDetails(
    @Id
    private val username: String,
    @Column(nullable = false)
    private val password: String,
) : UserDetails {
    override fun getAuthorities() = emptyList<GrantedAuthority>()

    override fun getPassword() = password

    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
