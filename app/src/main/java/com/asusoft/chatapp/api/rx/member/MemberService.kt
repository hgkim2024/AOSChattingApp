package com.asusoft.chatapp.api.rx.member

import com.asusoft.chatapp.api.domain.member.MemberCreateDto
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.domain.member.MemberReadDto
import com.asusoft.chatapp.api.rx.RetrofitClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable

object MemberService {

    private val memberRepository: MemberRepository = RetrofitClient.getInstanceAPI("MemberRepository") as MemberRepository
    private val objectMapper = ObjectMapper()

    fun signUp(dto: MemberCreateDto) : Observable<Long> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return memberRepository.signUp(map)
    }

    fun login(dto: LoginDto) : Observable<MemberReadDto> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return memberRepository.login(map)
    }

}