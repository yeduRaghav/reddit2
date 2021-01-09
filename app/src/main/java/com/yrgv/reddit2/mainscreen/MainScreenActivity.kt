package com.yrgv.reddit2.mainscreen

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.yrgv.reddit2.R
import com.yrgv.reddit2.utils.UiState

@Suppress("WHEN_ENUM_CAN_BE_NULL_IN_JAVA")
class MainScreenActivity : AppCompatActivity() {

    private val textView: TextView by lazy {
        findViewById(R.id.tv)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory()).get(MainScreenViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        setupViews()
        setupViewModelObservers()
    }

    private fun setupViews() {
        textView.setOnClickListener { viewModel.loadSubReddit("PS4") }
    }

    private fun setupViewModelObservers() {
        viewModel.getScreenState().observe(this, { uiState ->
            when (uiState) {
                UiState.LOADING -> textView.text = "Loading"
                UiState.ERROR -> textView.text = "error"
                UiState.LOADED -> textView.text = "DONE"
            }
        })

        viewModel.getPosts().observe(this, { posts ->
            textView.text = posts.size.toString()
        })
    }

}
