package com.asusoft.chatapp.util.api.domain.chtting

import java.io.Serializable

class ChattingReadDto(
    var id: Long?,
    var message: String?,
    var memberId: Long?,
    var chatRoomId: Long?
): Serializable {

    override fun toString(): String {
        return "ChattingReadDto(id=$id, message=$message, memberId=$memberId, chatRoomId=$chatRoomId)"
    }

}