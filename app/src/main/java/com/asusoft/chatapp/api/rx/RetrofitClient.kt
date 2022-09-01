package com.asusoft.chatapp.api.rx

import com.asusoft.chatapp.api.rx.member.MemberRepository

object RetrofitClient {

    val url = "http://172.30.1.10:8080/"
//    val url = "http://3.37.113.193:8080/"

    fun getInstanceAPI(obj: String): Any {
        val retrofit = RetrofitBuilder(url).build()

        return when (obj) {
            "MemberRepository" -> {
                retrofit.create(MemberRepository::class.java)
            }

            else -> throw Exception("getInstanceAPI error")
        }
    }

}