package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent

fun Activity.hideKeyboard(){
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}


fun Activity.isKeyboardOpen(): Boolean {
    return KeyboardVisibilityEvent.isKeyboardVisible(this) == true

}

fun Activity.isKeyboardClosed(): Boolean {
    return !KeyboardVisibilityEvent.isKeyboardVisible(this) == true
}
