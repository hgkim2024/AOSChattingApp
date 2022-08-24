package com.asusoft.chatapp.eventbus

import com.asusoft.chatapp.api.rx.member.MemberApi
import com.asusoft.chatapp.api.status.StatusCode

enum class BusMap(val value:String) {

    TITLE("title"),
    STATUS("status"),
    RESULT("result"),
    ERROR("error");

    companion object {
        fun post(
            title: String,
            status: StatusCode,
            result: Any
        ) {
            val map = HashMap<String, Any>()
            map[TITLE.value] = title
            map[STATUS.value] = status
            map[RESULT.value] = result

            GlobalBus.post(map)
        }
    }

}