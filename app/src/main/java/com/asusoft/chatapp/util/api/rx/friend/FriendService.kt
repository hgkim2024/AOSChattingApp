package com.asusoft.chatapp.util.api.rx.friend

import com.asusoft.chatapp.util.api.domain.friend.FriendCreateDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.RetrofitClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable

object FriendService {

    private val friendRepository: FriendRepository = RetrofitClient.getInstanceAPI("FriendRepository") as FriendRepository
    private val objectMapper = ObjectMapper()

    fun addFriend(dto: FriendCreateDto): Observable<Long> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return friendRepository.addFriend(map)
    }

    fun getFriendList(memberId: Long): Observable<List<MemberReadDto>> {
        return friendRepository.getFriendList(memberId)
    }

    fun removeFriend(memberId: Long, friendId: Long): Observable<Long> {
        return friendRepository.removeFriend(memberId, friendId)
    }

}