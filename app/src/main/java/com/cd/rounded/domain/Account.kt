package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * Account data class mapping the response keys
 */
data class Account(
    @SerializedName("accountUid") var accountUid: String? = "",
    @SerializedName("accountType") var accountType: String? ="",
    @SerializedName("defaultCategory") var defaultCategory: String? ="",
    @SerializedName("currency") var currency: String? = "",
    @SerializedName("createdAt") var createdAt: String? = "",
    @SerializedName("name") var name: String? = ""
)
