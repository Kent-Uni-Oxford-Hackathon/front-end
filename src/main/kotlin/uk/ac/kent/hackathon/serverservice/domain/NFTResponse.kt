package uk.ac.kent.hackathon.serverservice.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class NFTResponse(
    @JsonProperty("blockNumber") val blockNumber: Int,
    @JsonProperty("timeStamp") val timestamp: Int,
    @JsonProperty("hash") val hash: String,
    @JsonProperty("nonce") val nonce: Int,
    @JsonProperty("blockHash") val blockHash: String,
    @JsonProperty("from") val from: String,
    @JsonProperty("to") val to: String,
    @JsonProperty("tokenID") val tokenId: String,
    @JsonProperty("tokenName") val tokenName: String,
    @JsonProperty("tokenSymbol") val tokenSymbol: String,
    @JsonProperty("tokenDecimal") val tokenDecimal: String,
    @JsonProperty("transactionIndex") val transactionIndex: String,
    @JsonProperty("gas") val gas: String,
    @JsonProperty("gasPrice") val gasPrice: String,
    @JsonProperty("gasUsed") val gasUsed: String,
    @JsonProperty("cumulativeGasUsed") val cumulativeGasUsed: String,
    @JsonProperty("input") val input: String,
    @JsonProperty("confirmations") val confirmations: String,
)