package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * Savings Goal data class mapping the response keys
 */
data class SavingsGoal(
    @SerializedName("savingsGoalUid") var savingsGoalUid: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("totalSaved") var totalSaved: Amount? = null,
)
