package com.asusoft.chatapp.util.api.domain.chatroom

import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import java.io.Serializable

class EntryReadDto(
    var id: Long?,
    var member: MemberReadDto?
): Serializable {
    override fun toString(): String {
        return "EntryReadDto(id=$id, member=$member)"
    }
}