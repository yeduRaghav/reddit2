package com.yrgv.reddit2.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yrgv.reddit2.data.network.api.endpoints.FeedEndpoint
import com.yrgv.reddit2.data.network.api.model.response.PostsResponse
import com.yrgv.reddit2.utils.model.toUiModels
import com.yrgv.reddit2.utils.resourceprovider.ResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainScreenViewModel(
    private val resourceProvider: ResourceProvider,
    private val feedEndpoint: FeedEndpoint
) : ViewModel() {

    private val screenState = MutableLiveData<UiState>()
    private val posts = MutableLiveData<List<PostUiModel>>()
    private lateinit var currentSubReddit: String
    private var currentSearchJob: Job? = null

    fun getScreenState(): LiveData<UiState> = screenState
    fun getPosts(): LiveData<List<PostUiModel>> = posts

    fun search(subReddit: String) {
        currentSubReddit = subReddit
        performSearch()
    }

    fun retry() {
        performSearch()
    }

    private fun performSearch() {
        screenState.postValue(UiState.LOADING)
        currentSearchJob?.cancel()
        currentSearchJob = viewModelScope.launch {
            fetchFeedFromApi(currentSubReddit)
        }
    }

    private suspend fun fetchFeedFromApi(subReddit: String) {
        withContext(Dispatchers.IO) {
            val response = feedEndpoint.apply { setData(subReddit) }.execute()
            response.getValueOrNull()?.let { value ->
                handleFetchedPosts(value.data.children)
            } ?: screenState.postValue(UiState.ERROR)
        }
    }

    private suspend fun handleFetchedPosts(posts: List<PostsResponse.Post>) {
        screenState.postValue(UiState.LOADED)
        this.posts.postValue(posts.toUiModels { resId ->
            resourceProvider.getString(resId)
        })
    }

}
