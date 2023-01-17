package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * FeedItem data class mapping the response keys
 */
data class FeedItem(
    @SerializedName("feedItemUid") var feedItemUid: String? = "",
    @SerializedName("categoryUid") var categoryUid: String? ="",
    @SerializedName("amount") var amount: Amount? = null,
    @SerializedName("sourceAmount") var sourceAmount: Amount? = null,
    @SerializedName("direction") var direction: String? = "",
    @SerializedName("updatedAt") var updatedAt: String? = "",
    @SerializedName("transactionTime") var transactionTime: String? = "",
    @SerializedName("settlementTime") var settlementTime: String? ="",
    @SerializedName("source") var source: String? = "",
    @SerializedName("sourceSubType") var sourceSubType: String? = "",
    @SerializedName("status") var status: String? = "",
    @SerializedName("transactingApplicationUserUid") var transactingApplicationUserUid: String? = "",
    @SerializedName("counterPartyType") var counterPartyType: String? = "",
    @SerializedName("counterPartyUid") var counterPartyUid: String? ="",
    @SerializedName("counterPartyName") var counterPartyName: String? = "",
    @SerializedName("counterPartySubEntityUid") var counterPartySubEntityUid: String? = "",
    @SerializedName("reference") var reference: String? = "",
    @SerializedName("country") var country: String? = "",
    @SerializedName("spendingCategory") var spendingCategory: String? = "",
    @SerializedName("hasAttachment") var hasAttachment: String? = "",
    @SerializedName("hasReceipt") var hasReceipt: String? = "",
    @SerializedName("batchPaymentDetails") var batchPaymentDetails: String? = "",
    @SerializedName("roundUp") var roundUpDetails: AssociatedFeedRoundUp? = null,
)