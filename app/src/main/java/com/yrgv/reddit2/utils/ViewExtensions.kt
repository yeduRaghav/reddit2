package com.yrgv.reddit2.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * Extension functions for the View classes for convenience
 */

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}


/**
 * Click listener for Views with debounce.
 * */
fun View.setThrottledClickListener(delayInMillis: Long = 500L, runWhenClicked: SimpleCallback) {
    setOnClickListener {
        this.isClickable = false
        this.postDelayed({ this.isClickable = true }, delayInMillis)
        runWhenClicked()
    }
}


fun RecyclerView.setOnBottomReachedListener(listener: SimpleCallback) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val directionDown = 1
            if (!recyclerView.canScrollVertically(directionDown)) {
                listener()
            }
        }
    })
}