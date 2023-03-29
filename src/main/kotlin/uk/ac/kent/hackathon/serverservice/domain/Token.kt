package uk.ac.kent.hackathon.serverservice.domain

import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl

data class Token(
    val tokenId: Long,
    var owner: UserDetailsImpl,
    val description: String,
)