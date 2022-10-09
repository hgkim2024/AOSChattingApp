package com.asusoft.chatapp.util.recyclerview

import com.asusoft.chatapp.activity.chatting.ChattingActivity
import com.asusoft.chatapp.fragment.ChatRoomFragment
import com.asusoft.chatapp.fragment.FriendFragment

enum class RecyclerViewType(val value: Int) {


    FRIEND(0),
    CHATROOM(1),
    CHATTING(2),

    DEFAULT(999);


    companion object {
        fun getType(typeObject: Any): RecyclerViewType {
            return when(typeObject) {
                is FriendFragment -> FRIEND
                is ChatRoomFragment -> CHATROOM
                is ChattingActivity -> CHATTING
                else -> DEFAULT
            }
        }
    }
}