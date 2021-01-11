package com.yrgv.reddit2.mainscreen

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yrgv.reddit2.R
import com.yrgv.reddit2.data.network.api.RedditApi
import com.yrgv.reddit2.utils.ErrorView
import com.yrgv.reddit2.utils.hide
import com.yrgv.reddit2.utils.resourceprovider.DefaultResourceProvider
import com.yrgv.reddit2.utils.show

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class MainScreenActivity : AppCompatActivity() {
    private lateinit var errorView: ErrorView
    private lateinit var loadingView: ProgressBar
    private lateinit var recyclerview: RecyclerView

    private val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory(
                RedditApi.getInstance(),
                DefaultResourceProvider.getInstance(this.application)
            )
        ).get(MainScreenViewModel::class.java)
    }

    private val listAdapter = PostsListAdapter { post ->
        Toast.makeText(this, post.author, Toast.LENGTH_LONG).show()
        //todo:
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        setupViews()
        setupViewModelObservers()
        viewModel.search("PS4")
    }

    private fun setupViews() {
        errorView = findViewById(R.id.main_screen_error_view)
        errorView.setButtonClickListener { viewModel.retry() }
        recyclerview = findViewById(R.id.main_screen_recycler_view)
        recyclerview.adapter = listAdapter
        loadingView = findViewById(R.id.main_screen_loading_view)
    }

    private fun setupViewModelObservers() {
        viewModel.getPosts().observe(this, { posts ->
            listAdapter.submitList(posts)
        })
        viewModel.getScreenState().observe(this, { state ->
            handleUiState(state)
        })
    }

    private fun handleUiState(state: UiState) {
        when (state) {
            UiState.LOADING -> showLoadingView()
            UiState.ERROR -> showErrorView()
            UiState.LOADED -> showRecyclerView()
        }
    }

    /**
     * todo:
     * commit 3
     * build pagination
     *
     * commit 4
     * build detail activity
     * build detail view model
     * build detail api call
     * show detail
     *
     * commit 5
     * Build repository for feeds
     * handle reload, pagination
     *
     * commit 6
     * convert to Dagger
     *
     * commit 7
     * Test everything
     * */

    private fun showLoadingView() {
        errorView.hide()
        recyclerview.hide()
        loadingView.show()
    }

    private fun showRecyclerView() {
        errorView.hide()
        loadingView.hide()
        recyclerview.show()
    }

    private fun showErrorView() {
        loadingView.hide()
        recyclerview.hide()
        errorView.show()
    }

}