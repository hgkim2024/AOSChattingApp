package com.asusoft.chatapp.api.domain.member

class ReadMemberDto(
    var name: String?
) {
    override fun toString(): String {
        return "ReadMemberDto(name=$name)"
    }
}
