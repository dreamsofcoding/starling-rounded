package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * Accounts Response data class mapping the response keys
 */
data class AccountsResponse(
    @SerializedName("accounts") var accounts: List<Account>? = null,
)