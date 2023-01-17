package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * Associated Feed Round Up data class mapping the response keys
 */
data class AssociatedFeedRoundUp(
    @SerializedName("goalCategoryUid") var goalCategoryUid: String? = "",
    @SerializedName("amount") var associatedFeedRoundUpAmount: Amount? = null,
)