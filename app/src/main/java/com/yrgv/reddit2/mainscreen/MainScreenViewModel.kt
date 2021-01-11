package com.yrgv.reddit2.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yrgv.reddit2.data.network.api.endpoints.FeedEndpoint
import com.yrgv.reddit2.data.network.api.model.response.PostsResponse
import com.yrgv.reddit2.data.network.endpoint.EndpointError
import com.yrgv.reddit2.utils.Either
import com.yrgv.reddit2.utils.model.toUiModels
import com.yrgv.reddit2.utils.resourceprovider.ResourceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val resourceProvider: ResourceProvider,
    private val feedEndpoint: FeedEndpoint
) : ViewModel() {

    private val screenState = MutableLiveData<UiState>()
    private val postsToDisplay = MutableLiveData<List<PostUiModel>>()

    private lateinit var currentSubReddit: String
    private var pageAfter: String? = null
    private var feedLoadJob: Job? = null

    fun getScreenState(): LiveData<UiState> = screenState
    fun getPosts(): LiveData<List<PostUiModel>> = postsToDisplay

    //todo: test job life cycle handling
    fun search(subReddit: String) {
        feedLoadJob?.cancel()
        currentSubReddit = subReddit
        pageAfter = null
        screenState.postValue(UiState.LOADING)
        loadNextPage()
    }

    fun retry() {
        feedLoadJob?.cancel()
        screenState.postValue(UiState.LOADING)
        loadNextPage()
    }

    fun onPageBottomReached() {
        if (pageAfter == null) return
        if (feedLoadJob?.isActive == true) return
        loadNextPage()
    }

    private fun loadNextPage() {
        feedLoadJob = viewModelScope.launch {
            val response = feedEndpoint.apply {
                setData(currentSubReddit, pageAfter)
            }.execute()
            handlePageResponse(response)
        }
    }

    private suspend fun handlePageResponse(response: Either<EndpointError, PostsResponse>) {
        when (response) {
            is Either.Error -> handleResponseFailure(response.error)
            is Either.Value -> handleFeedResponseSuccess(response.value)
        }
    }

    private suspend fun handleFeedResponseSuccess(response: PostsResponse) {
        val freshPosts = response.data.children.toUiModels { resourceProvider.getString(it) }
        if (pageAfter == null) { //first page
            postsToDisplay.postValue(freshPosts)
        } else { //consecutive page
            postsToDisplay.postValue(getAppendedPostsToDisplay(freshPosts))
        }
        pageAfter = response.data.after
        screenState.postValue(UiState.LOADED)
    }

    /**
     * @param freshPosts Items to be appended on to the existing list in postsToDisplay
     * @return A list with items appended to the original items in postsToDisplay
     * */
    private fun getAppendedPostsToDisplay(freshPosts: List<PostUiModel>): List<PostUiModel> {
        return mutableListOf<PostUiModel>().apply {
            postsToDisplay.value?.toList()?.let {
                addAll(it)
                addAll(freshPosts)
            }
        }
    }

    private fun handleResponseFailure(error: EndpointError) {
        when (error) {
            is EndpointError.Unreachable,
            is EndpointError.UnhandledError,
            is EndpointError.ClientError.BadRequest,
            is EndpointError.ClientError.Unauthorised,
            is EndpointError.ClientError.Forbidden,
            is EndpointError.ClientError.NotFound,
            is EndpointError.ClientError.Timeout,
            is EndpointError.ClientError.IAmATeapot,
            is EndpointError.ServerError.InternalServerError,
            is EndpointError.ServerError.ServiceUnavailable -> {
                if (pageAfter == null) {
                    screenState.postValue(UiState.ERROR)
                }
            }
        }
    }
}
