package com.asusoft.chatapp.util.api.domain.chatroom

import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import java.io.Serializable
import kotlin.contracts.contract

class ChatRoomReadDto(
    var id: Long?,
    var name: String,
    var entryList: List<EntryReadDto>?
): Serializable {
    override fun toString(): String {
        return "ChatRoomReadDto(id=$id, name='$name', entryList=$entryList)"
    }

    fun getFriend(myInfo: MemberReadDto?): MemberReadDto? {
        myInfo ?: return null
        entryList ?: return null

        var friend: MemberReadDto? = null
        entryList?.forEach lit@{
            if (it.member?.id != null) {
                if (myInfo.id != it.member?.id) {
                    friend = it.member
                }
            }
        }

        return friend
    }
}