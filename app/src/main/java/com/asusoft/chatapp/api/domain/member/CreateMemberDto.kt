package com.asusoft.chatapp.api.domain.member

class CreateMemberDto {
    var name: String? = null
    var id: String? = null
    var pw: String? = null

    constructor(name: String?, id: String?, pw: String?) {
        this.name = name
        this.id = id
        this.pw = pw
    }
}