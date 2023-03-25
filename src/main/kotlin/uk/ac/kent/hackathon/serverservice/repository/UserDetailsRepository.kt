package uk.ac.kent.hackathon.serverservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import uk.ac.kent.hackathon.serverservice.entities.UserDetails

interface UserDetailsRepository : JpaRepository<UserDetails, String>
