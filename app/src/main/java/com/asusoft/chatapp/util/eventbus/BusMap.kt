package com.asusoft.chatapp.util.eventbus

import com.asusoft.chatapp.util.api.status.StatusCode

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