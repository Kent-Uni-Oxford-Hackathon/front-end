package uk.ac.kent.hackathon.serverservice.entities

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
data class Token(
    @Id
    val tokenId: String,
    @Column(nullable = false)
    val tokenString: String,
    @OneToOne
    val owner: UserDetailsImpl,
    @Column(nullable = false)
    @ElementCollection
    val categories: Collection<String>,
    @Column(nullable = false)
    val timestamp: Timestamp,
    @Column(nullable = false)
    val description: String,
)