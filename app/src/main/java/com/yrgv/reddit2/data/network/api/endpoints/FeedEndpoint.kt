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
    private var after: String? = null

    fun setData(subreddit: String, after: String? = null) {
        this.subreddit = subreddit
        this.after = after
    }

    override fun getCall(): Call<PostsResponse> {
        return redditApi.getPosts(subreddit, after)
    }
}