package com.asusoft.chatapp.util.recyclerview.holder

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.asusoft.chatapp.R
import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto
import com.asusoft.chatapp.util.extension.imageLoad
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChattingFriendHolder(
    private val view: View
): RecyclerView.ViewHolder(view) {
    fun bind(
        position: Int,
        adapter: RecyclerViewAdapter
    ) {
        val item = adapter.list[position] as? ChattingReadDto ?: return

        val tv = view.findViewById<TextView>(R.id.tv)
        val iv = view.findViewById<ImageView>(R.id.iv)
        val title = view.findViewById<TextView>(R.id.title)

        tv.text = item.message
        iv.imageLoad(view, adapter.friendInfo?.profileUrl, R.drawable.ic_person_24)
        title.text = adapter.friendInfo?.name

        val time = view.findViewById<TextView>(R.id.time)
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val localDateTime = LocalDateTime.parse(item.createdTime, pattern)
        val text = if (localDateTime.hour >= 12) "오후" else "오전"
        time.text = "$text ${localDateTime.hour%12}:${localDateTime.minute}"
    }
}