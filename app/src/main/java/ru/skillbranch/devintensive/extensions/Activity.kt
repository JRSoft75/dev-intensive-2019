package ru.skillbranch.devintensive.extensions

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(){
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}


fun Activity.isKeyboardOpen(): Boolean {
    return isKeyboardVisible(this) == true

}

fun Activity.isKeyboardClosed(): Boolean {
    return !isKeyboardVisible(this) == true
}

fun isKeyboardVisible(activity: Activity): Boolean {
    val KEYBOARD_MIN_HEIGHT_RATIO = 0.15
    val r = Rect()
    val activityRoot = getActivityRoot(activity)
    activityRoot.getWindowVisibleDisplayFrame(r)
    val screenHeight = activityRoot.rootView.height
    val heightDiff = screenHeight - r.height()
    return heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO
}

fun getActivityRoot(activity: Activity): View {
    return (activity.findViewById<View>(R.id.content) as ViewGroup).getChildAt(
        0
    )
}


