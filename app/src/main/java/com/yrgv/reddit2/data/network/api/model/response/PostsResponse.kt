package com.yrgv.reddit2.data.network.api.model.response

import com.google.gson.annotations.SerializedName

/**
 * Defines the Api Response models with only necessary value.
 */

data class PostsResponse(@SerializedName("data") val data: PostsResponseData) {

    data class PostsResponseData(
        @SerializedName("children") val children: List<Post>,
        @SerializedName("after") val after: String?
    )

    data class Post(@SerializedName("data") val data: PostData) {
        data class PostData(
            @SerializedName("id") val id: String,
            @SerializedName("author") val author: String,
            @SerializedName("title") val title: String,
            @SerializedName("selftext") val selftext: String,
            @SerializedName("thumbnail") val thumbnail: String
        )
    }
}