package com.asusoft.chatapp.util.api.domain.member

import java.io.Serializable

class MemberReadDto(
    var id: Long?,
    var name: String?,
    var profileUrl: String?
): Serializable {
    override fun toString(): String {
        return "MemberReadDto(id=$id, name=$name, profileUrl=$profileUrl)"
    }
}
