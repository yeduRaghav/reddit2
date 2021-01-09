package com.yrgv.reddit2.utils

import android.util.Log
import com.yrgv.reddit2.BuildConfig

/**
 * Functions to help debug
 */
fun logThread(title: String) {
    if (!BuildConfig.DEBUG) return
    Log.d("appan", "$title ==> ${Thread.currentThread().name}")
}