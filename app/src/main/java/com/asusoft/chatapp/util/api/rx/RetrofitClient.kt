package com.asusoft.chatapp.util.api.rx

import com.asusoft.chatapp.util.api.rx.chatroom.ChatRoomRepository
import com.asusoft.chatapp.util.api.rx.chtting.ChattingRepository
import com.asusoft.chatapp.util.api.rx.friend.FriendRepository
import com.asusoft.chatapp.util.api.rx.member.MemberRepository
import com.asusoft.chatapp.util.api.rx.profile.ProfileRepository

object RetrofitClient {

    val url = "http://172.30.1.10:8080/"
//    val url = "http://3.37.113.193:8080/"

    fun getInstanceAPI(obj: String): Any {
        val retrofit = RetrofitBuilder(url).build()

        return when (obj) {
            "MemberRepository" -> {
                retrofit.create(MemberRepository::class.java)
            }

            "FriendRepository" -> {
                retrofit.create(FriendRepository::class.java)
            }

            "ProfileRepository" -> {
                retrofit.create(ProfileRepository::class.java)
            }

            "ChatRoomRepository" -> {
                retrofit.create(ChatRoomRepository::class.java)
            }

            "ChattingRepository" -> {
                retrofit.create(ChattingRepository::class.java)
            }

            else -> throw Exception("getInstanceAPI error")
        }
    }

}