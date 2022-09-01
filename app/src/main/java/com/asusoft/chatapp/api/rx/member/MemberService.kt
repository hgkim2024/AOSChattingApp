package com.asusoft.chatapp.api.rx.member

import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.domain.member.ReadMemberDto
import com.asusoft.chatapp.api.rx.RetrofitClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable

object MemberService {

    private val memberRepository: MemberRepository = RetrofitClient.getInstanceAPI("MemberRepository") as MemberRepository
    private val objectMapper = ObjectMapper()

    fun signUp(dto: CreateMemberDto) : Observable<Long> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return memberRepository.signUp(map)
    }

    fun login(dto: LoginDto) : Observable<ReadMemberDto> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return memberRepository.login(map)
    }

}