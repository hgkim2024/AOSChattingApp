package com.asusoft.chatapp.api.domain.member

import java.io.Serializable

class MemberReadDto(
    var id: Long?,
    var name: String?
): Serializable {
    override fun toString(): String {
        return "MemberReadDto(id=$id, name=$name)"
    }
}
