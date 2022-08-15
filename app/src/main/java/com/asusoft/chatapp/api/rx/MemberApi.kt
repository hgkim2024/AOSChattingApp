package com.asusoft.chatapp.api.rx

import io.reactivex.Maybe
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface MemberApi {

    @POST("member/signup")
    fun signUp(
        @QueryMap map: Map<String, String>
    ): Maybe<Response<ResponseBody>>

}