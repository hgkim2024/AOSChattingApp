package com.asusoft.chatapp.util.api.domain.chtting

import java.io.Serializable

class ChattingCreateDto(
    var message: String?,
    var memberId: Long?,
    var chatRoomId: Long?
): Serializable {

    override fun toString(): String {
        return "ChattingCreateDto(message=$message, memberId=$memberId, chatRoomId=$chatRoomId)"
    }

}