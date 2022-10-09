package com.asusoft.chatapp.util.recyclerview.holder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asusoft.chatapp.R
import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomReadDto
import com.asusoft.chatapp.util.extension.imageLoad
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter

class ChatRoomHolder(
    private val view: View
): RecyclerView.ViewHolder(view) {
    fun bind(
        position: Int,
        adapter: RecyclerViewAdapter
    ) {
        val item = adapter.list[position] as? ChatRoomReadDto ?: return
        val friend = item.getFriend(adapter.myInfo)

        friend ?: return

        val tv = view.findViewById<TextView>(R.id.tv)
        tv.text = friend.name

        val iv = view.findViewById<ImageView>(R.id.iv)

        iv.imageLoad(view, friend.profileUrl, R.drawable.ic_person_24)
    }
}