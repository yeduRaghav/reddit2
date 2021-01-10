package com.yrgv.reddit2.mainscreen

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.yrgv.reddit2.utils.PostClickListener

/**
 * RecyclerView Adapter that renders the list of posts.
 */
class PostsListAdapter(private val postClickListener: PostClickListener) :
    ListAdapter<PostUiModel, PostViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<PostUiModel>() {
        override fun areItemsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PostUiModel, newItem: PostUiModel): Boolean {
            return (oldItem.title == newItem.title) && (oldItem.description == newItem.description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.get(parent)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), postClickListener)
    }

}