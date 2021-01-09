package com.yrgv.reddit2.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yrgv.reddit2.data.network.api.RedditApi
import com.yrgv.reddit2.data.network.api.endpoints.FeedEndpoint
import com.yrgv.reddit2.data.network.api.model.response.PostsResponse
import com.yrgv.reddit2.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    private val feedEndpoint = FeedEndpoint(RedditApi.build())
    private val screenState = MutableLiveData<UiState>()
    private val posts = MutableLiveData<List<PostsResponse.Post>>()

    fun getScreenState(): LiveData<UiState> = screenState
    fun getPosts(): LiveData<List<PostsResponse.Post>> = posts

    fun loadSubReddit(subReddit: String) {
        screenState.postValue(UiState.LOADING)
        viewModelScope.launch(Dispatchers.IO) {
            fetchFeedFromApi(subReddit)
        }
    }

    private suspend fun fetchFeedFromApi(subReddit: String) {
        val response = feedEndpoint.apply {
            setData(subReddit)
        }.execute()

        response.getValueOrNull()?.let { value ->
            handlePosts(value.data.children)
        } ?: screenState.postValue(UiState.ERROR)
    }

    private fun handlePosts(posts: List<PostsResponse.Post>) {
        screenState.postValue(UiState.LOADED)
        this.posts.postValue(posts)
    }

}