package com.yrgv.reddit2.mainscreen

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.yrgv.reddit2.R
import com.yrgv.reddit2.data.network.api.RedditApi
import com.yrgv.reddit2.utils.hide
import com.yrgv.reddit2.utils.resourceprovider.DefaultResourceProvider
import com.yrgv.reddit2.utils.show

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class MainScreenActivity : AppCompatActivity() {
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
        viewModel.loadSubReddit("PS4")
    }

    private fun setupViews() {
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

    private fun showLoadingView() {
        recyclerview.hide()
        loadingView.show()
    }

    private fun showRecyclerView() {
        loadingView.hide()
        recyclerview.show()
    }

    private fun showErrorView() {
        //todo:
        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
    }

}