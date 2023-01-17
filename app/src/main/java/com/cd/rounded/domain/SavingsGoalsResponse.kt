package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * Savings Goals Response data class mapping the response keys
 */
data class SavingsGoalsResponse(
    @SerializedName("savingsGoalList") var savingsGoalList: List<SavingsGoal>? = null,
)