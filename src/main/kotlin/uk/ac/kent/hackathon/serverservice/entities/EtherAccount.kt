package uk.ac.kent.hackathon.serverservice.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class EtherAccount (
    @Id
    var ethPkHash: String
)