package com.asusoft.chatapp.api.domain.member

import java.io.Serializable

class MemberReadDto(
    var name: String?
): Serializable {
    override fun toString(): String {
        return "ReadMemberDto(name=$name)"
    }
}
