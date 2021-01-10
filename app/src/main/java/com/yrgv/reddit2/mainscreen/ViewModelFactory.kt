package com.yrgv.reddit2.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yrgv.reddit2.data.network.api.RedditApi
import com.yrgv.reddit2.data.network.api.endpoints.FeedEndpoint
import com.yrgv.reddit2.utils.resourceprovider.ResourceProvider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val redditApi: RedditApi,
    private val resourceProvider: ResourceProvider
) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainScreenViewModel::class.java)) {
            return MainScreenViewModel(resourceProvider, FeedEndpoint(redditApi)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class :${modelClass.canonicalName}")
    }
}