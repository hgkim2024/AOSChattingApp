package com.asusoft.chatapp.util.extension

import android.view.View
import android.view.ViewGroup

fun View.removeFromSuperView() {
    val viewGroup = parent as? ViewGroup ?: return

    if (viewGroup.childCount > 0) {
        viewGroup.removeView(this)
    }
}