package uk.ac.kent.hackathon.serverservice.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenNFTTxResponse(
    @JsonProperty("status") val status: Int,
    @JsonProperty("message") val message: String,
    @JsonProperty("result") val result: List<NFTResponse>,
)
