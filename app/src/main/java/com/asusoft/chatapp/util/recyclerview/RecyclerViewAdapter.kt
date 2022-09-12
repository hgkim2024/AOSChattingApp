package com.asusoft.chatapp.util.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asusoft.chatapp.R
import com.asusoft.chatapp.util.recyclerview.RecyclerViewType.*
import com.asusoft.chatapp.util.recyclerview.holder.EmptyHolder
import com.asusoft.chatapp.util.recyclerview.holder.FriendHolder

class RecyclerViewAdapter(
    private val typeObject: Any,
    var list: ArrayList<Any>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        // 클릭 이펙트 딜레이
        const val CLICK_DELAY = 150L
    }

    private val type = RecyclerViewType.getType(typeObject)

    override fun getItemViewType(position: Int): Int {
        return getType(position)
    }

    private fun getType(position: Int): Int {
        val item = list[position]

        return when(type) {
            else -> 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val ctx = parent.context
        val inflater = LayoutInflater.from(ctx)

        return when(type) {
            FRIEND -> {
                val view = inflater.inflate(R.layout.list_friend, parent, false)
                FriendHolder(view)
            }

            DEFAULT -> {
                val view = View(ctx)
                EmptyHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(type) {
            FRIEND -> (holder as FriendHolder).bind(position, this)
            DEFAULT -> (holder as EmptyHolder).bind(position, this)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}