package com.asusoft.chatapp.api.rx.friend

import com.asusoft.chatapp.api.domain.member.MemberReadDto
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FriendRepository {

    @POST("friend/addFriend")
    fun addFriend(
        @QueryMap map: Map<String, String>
    ): Observable<Long>

    @GET("friend")
    fun getFriendList(
        @Query("memberId") memberId: Long
    ): Observable<List<MemberReadDto>>

    @POST("delete")
    fun removeFriend(
        @Query("friendId") friendId: Long
    ): Observable<Long>

}