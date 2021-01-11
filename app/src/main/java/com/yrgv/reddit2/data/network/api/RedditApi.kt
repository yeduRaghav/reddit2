package com.yrgv.reddit2.data.network.api

import android.util.Log
import com.yrgv.reddit2.data.network.api.model.response.PostsResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit Api definition.
 */

interface RedditApi {

    companion object {
        private const val BASE_URL = "https://www.reddit.com/r/"
        private const val RESPONSE_TYPE_JSON = ".json"

        private const val PATH_VAR_SUBREDDIT = "subReddit"
        private const val QUERY_AFTER = "after"
        private const val PATH_SUBREDDIT = "{$PATH_VAR_SUBREDDIT}$RESPONSE_TYPE_JSON"

        private lateinit var apiInstance: RedditApi

        fun getInstance(): RedditApi {
            if (!::apiInstance.isInitialized) {
                apiInstance = build()
            }
            return apiInstance
        }

        private fun build(): RedditApi {
            val okhttpClient = OkHttpClient.Builder()
                .addInterceptor(getLoggingInterceptor())
                .build()
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okhttpClient)
                .build()
                .create(RedditApi::class.java)
        }

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Log.i("Guruve", message)
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }


    @GET(PATH_SUBREDDIT)
    fun getPosts(
        @Path(PATH_VAR_SUBREDDIT) subReddit: String,
        @Query(QUERY_AFTER) after: String?,
    ): Call<PostsResponse>

}