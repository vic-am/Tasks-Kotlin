package com.example.tasks.service

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

class Utls {

    companion object {
        fun closeKeyboard(context: Context, activity: Activity) {
            val imm: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
        }
    }

}