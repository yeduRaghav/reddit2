package com.yrgv.reddit2.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.yrgv.reddit2.R

/**
 *  A simple view that can be used for error cases, shows a message and a button.
 */
class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_view_error, this, true)
    }

    private val button = findViewById<MaterialButton>(R.id.error_view_button)
    private val messageView = findViewById<MaterialTextView>(R.id.error_view_message_text_view)

    fun setButtonClickListener(callback: SimpleCallback) {
        button.setThrottledClickListener {
            callback()
        }
    }

    fun setMessageText(@StringRes resId: Int) {
        messageView.text = context.getText(resId)
    }

    fun setButtonText(@StringRes resId: Int) {
        button.text = context.getText(resId)
    }

}