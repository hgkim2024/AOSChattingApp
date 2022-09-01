package com.asusoft.chatapp.api.rx.member

import com.asusoft.chatapp.api.domain.member.ReadMemberDto
import io.reactivex.Maybe
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface MemberRepository {

    @POST("member/signup")
    fun signUp(
        @QueryMap map: Map<String, String>
    ): Observable<Long>


    @POST("member/login")
    fun login(
        @QueryMap map: Map<String, String>
    ): Observable<ReadMemberDto>
}