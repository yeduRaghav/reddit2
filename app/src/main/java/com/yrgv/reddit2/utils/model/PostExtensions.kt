package com.yrgv.reddit2.utils.model

import com.yrgv.reddit2.R
import com.yrgv.reddit2.data.network.api.model.response.PostsResponse
import com.yrgv.reddit2.mainscreen.PostUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Holds extension function to help with Post models
 */
fun PostsResponse.Post.PostData.toUiModel(stringProvider: (resId: Int) -> String?): PostUiModel {
    return PostUiModel(
        id = id,
        author = author.getLocalizedAuthor(stringProvider),
        title = title,
        description = selftext,
        thumbnail = thumbnail.getSanitizedThumbnailUrl()
    )
}

/**
 * Prepends "u/" to the author's name
 * */
fun String.getLocalizedAuthor(
    stringProvider: (resourceId: Int) -> String?
): String {
    return stringProvider(R.string.user_prefix)?.plus(this) ?: this
}


/**
 * This is necessary because the url from api can be empty, contain
 * just whitespace or in some cases say 'self' based on the Post category.
 * */
fun String.getSanitizedThumbnailUrl(): String? {
    return this.takeIf { startsWith("https://", true) }
}


suspend fun List<PostsResponse.Post>.toUiModels(stringProvider: (resId: Int) -> String?): List<PostUiModel> {
    return withContext(Dispatchers.Default) {
        mapTo(arrayListOf()) { it.data.toUiModel(stringProvider) }
    }
}
