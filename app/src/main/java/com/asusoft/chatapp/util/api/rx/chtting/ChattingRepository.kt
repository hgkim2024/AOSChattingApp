package com.asusoft.chatapp.util.api.rx.chtting

import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ChattingRepository {

    @POST("chatting/create")
    fun create(
        @QueryMap map: Map<String, String>
    ): Observable<Long>

    @GET("chatting/list")
    fun list(
        @Query("chatroomId") chatroomId: Long
    ): Observable<List<ChattingReadDto>>

}