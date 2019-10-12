package com.tamimattafi.navigationmanager.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun isKeyboardVisible(context: Context): Boolean {
        (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.apply {
            return InputMethodManager::class.java.getMethod("getInputMethodWindowVisibleHeight").invoke(
                this
            ) as Int > 0
        }
        return false
    }

    fun hide(context: Context) {
        if (isKeyboardVisible(context)) {
            (context as Activity).currentFocus?.let { v ->
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
        }
    }

    fun show(context: Context) {
        if (!isKeyboardVisible(context)) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}