package uk.ac.kent.hackathon.serverservice.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class NFTResponse(
    @JsonProperty("blockNumber") val blockNumber: Long,
    @JsonProperty("timeStamp") val timestamp: Long,
    @JsonProperty("hash") val hash: String,
    @JsonProperty("nonce") val nonce: Long,
    @JsonProperty("blockHash") val blockHash: String,
    @JsonProperty("from") val from: String,
    @JsonProperty("contractAddress") val contractAddress: String,
    @JsonProperty("to") val to: String,
    @JsonProperty("tokenID") val tokenId: Long,
    @JsonProperty("tokenName") val tokenName: String,
    @JsonProperty("tokenSymbol") val tokenSymbol: String,
    @JsonProperty("tokenDecimal") val tokenDecimal: Long,
    @JsonProperty("transactionIndex") val transactionIndex: Long,
    @JsonProperty("gas") val gas: Long,
    @JsonProperty("gasPrice") val gasPrice: Long,
    @JsonProperty("gasUsed") val gasUsed: Long,
    @JsonProperty("cumulativeGasUsed") val cumulativeGasUsed: Long,
    @JsonProperty("input") val input: String,
    @JsonProperty("confirmations") val confirmations: Long,
)