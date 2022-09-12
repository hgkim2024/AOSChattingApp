package com.asusoft.chatapp.util.recyclerview.holder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter

class EmptyHolder(
    private val view: View
): RecyclerView.ViewHolder(view) {
    fun bind(
        position: Int,
        adapter: RecyclerViewAdapter
    ) {
        view.setBackgroundColor(Color.GRAY)
    }
}