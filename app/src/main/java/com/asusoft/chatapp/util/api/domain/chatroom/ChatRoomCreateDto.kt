package com.asusoft.chatapp.util.api.domain.chatroom

class ChatRoomCreateDto(
    var name: String,
    var memberId: Long,
    var friendId: Long
) {
    override fun toString(): String {
        return "ChatRoomCreateDto(name='$name', memberId='$memberId', friendId='$friendId')"
    }
}