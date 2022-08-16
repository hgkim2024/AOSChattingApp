package com.asusoft.chatapp.api.rx.member

import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.domain.member.ReadMemberDto
import com.asusoft.chatapp.api.rx.RetrofitClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response


object MemberApiClient {

    private val retrofitClient: MemberApiInterface = RetrofitClient.getInstanceAPI("MemberApi") as MemberApiInterface
    private val objectMapper = ObjectMapper()

    fun signUp(dto: CreateMemberDto) : Maybe<Response<ResponseBody>> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return retrofitClient.signUp(map)
    }

    fun login(dto: LoginDto) : Maybe<Response<ReadMemberDto>> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return retrofitClient.login(map)
    }

}