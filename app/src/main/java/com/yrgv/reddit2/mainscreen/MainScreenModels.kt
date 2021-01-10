package com.yrgv.reddit2.mainscreen

/**
 * Defines the Data for Ui
 */
data class PostUiModel(
    val id: String,
    val author: String,
    val title: String,
    val description: String,
    val thumbnail: String?
)

enum class UiState {
    LOADING, ERROR, LOADED
}

