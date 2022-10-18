package com.asusoft.chatapp.util.api.domain.chtting

import java.io.Serializable

class ChattingReadDto(
    var id: Long? = -1,
    var message: String? = "",
    var memberId: Long? = -1,
    var chatRoomId: Long? = -1
): Serializable {

    override fun toString(): String {
        return "ChattingReadDto(id=$id, message=$message, memberId=$memberId, chatRoomId=$chatRoomId)"
    }

}