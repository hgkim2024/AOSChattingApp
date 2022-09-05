package com.asusoft.chatapp.api.domain.friend

class FriendCreateDto(
    var myId: Long?,
    var friendId: Long?
) {
    override fun toString(): String {
        return "FriendCreateDto(myId=$myId, friendId=$friendId)"
    }
}