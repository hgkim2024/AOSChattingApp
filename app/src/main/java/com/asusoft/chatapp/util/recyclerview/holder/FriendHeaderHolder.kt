package com.asusoft.chatapp.util.recyclerview.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asusoft.chatapp.R
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter

class FriendHeaderHolder(
    private val view: View
): RecyclerView.ViewHolder(view) {
    fun bind(
        position: Int,
        adapter: RecyclerViewAdapter
    ) {
        val item = adapter.list[position] as? String ?: return
        val tv = view.findViewById<TextView>(R.id.tv)
        tv.text = item
    }
}