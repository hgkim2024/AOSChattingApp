package com.asusoft.chatapp.util.api.rx.chatroom

import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomReadDto
import com.asusoft.chatapp.util.api.domain.chatroom.EntryReadDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface ChatRoomRepository {

    @POST("chatroom/create")
    fun create(
        @QueryMap map: Map<String, String>
    ): Observable<ChatRoomReadDto>

    @GET("chatroom/list")
    fun list(
        @Query("memberId") memberId:Long
    ): Observable<List<ChatRoomReadDto>>

}