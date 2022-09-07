package com.asusoft.chatapp.util.api.rx.member

import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import io.reactivex.Observable
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
    ): Observable<MemberReadDto>
}