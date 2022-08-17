package com.asusoft.chatapp.api.rx

import com.asusoft.chatapp.api.rx.member.MemberApiInterface

object RetrofitClient {

    val url = "http://172.30.1.10:8080/"
//    val url = "http://3.37.113.193:8080/"

//    private var BASE_URL = ""
//
//    fun setURL(baseString: String) {
//        BASE_URL = "http://$baseString:8080"
//    }
//
//    fun setURL(baseString: String, port: String) {
//        BASE_URL = "http://$baseString:$port"
//    }
//
//    fun setFullURL(baseString: String) {
//        BASE_URL = baseString
//    }

    fun getInstanceAPI(obj: String): Any {
        val retrofit = RetrofitBuilder(url).build()

        return when (obj) {
            "MemberApi" -> {
                retrofit.create(MemberApiInterface::class.java)
            }

            else -> throw Exception("getInstanceAPI error")
        }
    }

}