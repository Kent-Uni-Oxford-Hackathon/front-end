package uk.ac.kent.hackathon.serverservice.domain

import uk.ac.kent.hackathon.serverservice.entities.UserDetailsImpl

data class Token(
    val tokenId: String,
    var owner: UserDetailsImpl,
    val description: String,
)