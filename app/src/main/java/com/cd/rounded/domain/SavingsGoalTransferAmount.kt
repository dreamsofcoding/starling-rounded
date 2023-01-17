package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * Savings Goal Transfer Amount data class mapping the request object
 */
data class SavingsGoalTransferAmount(
    @SerializedName("amount") var amount: Amount? = null,
)