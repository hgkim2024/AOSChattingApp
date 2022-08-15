package com.asusoft.chatapp.api.rx

import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


object MemberApiClient {

    private val retrofitClient: MemberApi = RetrofitClient.getInstanceAPI("MemberApi") as MemberApi
    private val objectMapper = ObjectMapper()

    @JvmSuppressWildcards
    fun signUp(dto: CreateMemberDto) : Maybe<Response<ResponseBody>> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return retrofitClient.signUp(map)
    }

}