package uk.ac.kent.hackathon.serverservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class TokenDescriptionPair(
    @Id
    val tokenId: Long,
    @Column(nullable = false)
    val description: String
)