package com.yrgv.reddit2.data.network.api.endpoints

import com.yrgv.reddit2.data.network.api.RedditApi
import com.yrgv.reddit2.data.network.api.model.response.PostsResponse
import com.yrgv.reddit2.data.network.endpoint.BaseEndpoint
import retrofit2.Call

/**
 * Returns feed for a given subreddit
 */
class FeedEndpoint(private val redditApi: RedditApi) : BaseEndpoint<PostsResponse>() {

    private lateinit var subreddit: String

    fun setData(subreddit: String) {
        this.subreddit = subreddit
    }

    override fun getCall(): Call<PostsResponse> {
        return redditApi.getPosts(subreddit)
    }
}