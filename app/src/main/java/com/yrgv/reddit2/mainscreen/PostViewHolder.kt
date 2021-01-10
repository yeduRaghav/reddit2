package com.yrgv.reddit2.mainscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import com.yrgv.reddit2.R
import com.yrgv.reddit2.utils.PostClickListener
import com.yrgv.reddit2.utils.hide
import com.yrgv.reddit2.utils.setThrottledClickListener
import com.yrgv.reddit2.utils.show

/**
 *
 */
class PostViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LAYOUT_ID = R.layout.layout_post_list_item

        /**
         * Invoke this get an instance of the ViewHolder
         * */
        fun get(parent: ViewGroup): PostViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(LAYOUT_ID, parent, false)
            return PostViewHolder(itemView)
        }
    }

    private val authorView: MaterialTextView = itemView.findViewById(R.id.post_list_item_author)
    private val titleView: MaterialTextView = itemView.findViewById(R.id.post_list_item_title)
    private val thumbnailView: ImageView = itemView.findViewById(R.id.post_list_item_thumbnail)

    fun bind(post: PostUiModel, postClickListener: PostClickListener) {
        itemView.setThrottledClickListener {
            postClickListener(post)
        }
        authorView.text = post.author
        titleView.text = post.title
        post.thumbnail?.let {
            thumbnailView.show()
            Picasso.get()
                .load(it)
                .placeholder(R.drawable.post_thumbnail_placeholder)
                .into(thumbnailView)
        } ?: thumbnailView.hide()
    }

}