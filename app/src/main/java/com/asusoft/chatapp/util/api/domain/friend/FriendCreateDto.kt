package com.asusoft.chatapp.util.api.domain.friend

class FriendCreateDto(
    var myId: Long?,
    var friendName: String?
) {
    override fun toString(): String {
        return "FriendCreateDto(myId=$myId, friendName=$friendName)"
    }
}