package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * Amount data class mapping the response keys
 */
data class Amount(
    @SerializedName("currency") var currency: String? = "",
    @SerializedName("minorUnits") var minorUnits: Int? = 0,
)
