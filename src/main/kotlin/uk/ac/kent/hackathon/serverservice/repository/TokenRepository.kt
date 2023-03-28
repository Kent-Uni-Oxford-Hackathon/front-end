package uk.ac.kent.hackathon.serverservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.ac.kent.hackathon.serverservice.entities.Token

@Repository
interface TokenRepository : JpaRepository<Token, String>