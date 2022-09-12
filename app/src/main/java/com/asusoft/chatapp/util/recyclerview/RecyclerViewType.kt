package com.asusoft.chatapp.util.recyclerview

import com.asusoft.chatapp.fragment.FriendFragment

enum class RecyclerViewType(val value: Int) {


    FRIEND(0),


    DEFAULT(999);


    companion object {
        fun getType(typeObject: Any): RecyclerViewType {
            return when(typeObject) {
                is FriendFragment -> FRIEND
                else -> DEFAULT
            }
        }
    }
}