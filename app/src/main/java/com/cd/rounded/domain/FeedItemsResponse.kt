package com.cd.rounded.domain

import com.google.gson.annotations.SerializedName

/**
 * FeedItemsResponse data class mapping the response keys
 */
data class FeedItemsResponse(
    @SerializedName("feedItems") var feedItems: List<FeedItem>? = null,
)