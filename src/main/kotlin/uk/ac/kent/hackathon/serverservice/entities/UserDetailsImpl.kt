package uk.ac.kent.hackathon.serverservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
data class UserDetailsImpl(
    @Id
    private val username: String,
    @Column(nullable = false)
    private var password: String,
    @OneToOne var etherAccount: EtherAccount,
) : UserDetails {
    override fun getAuthorities() = emptyList<GrantedAuthority>()

    override fun getPassword() = password

    fun setPassword(newPassword: String) {
        password = newPassword
    }

    override fun getUsername() = username

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}
