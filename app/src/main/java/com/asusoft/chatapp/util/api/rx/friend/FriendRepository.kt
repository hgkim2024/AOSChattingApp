package com.asusoft.chatapp.util.api.rx.friend

import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FriendRepository {

    @POST("friend/addFriend")
    fun add(
        @QueryMap map: Map<String, String>
    ): Observable<Long>

    @GET("friend")
    fun list(
        @Query("memberId") memberId: Long
    ): Observable<List<MemberReadDto>>

    @POST("friend/delete")
    fun remove(
        @Query("memberId") memberId:Long,
        @Query("friendId") friendId: Long
    ): Observable<Long>

}