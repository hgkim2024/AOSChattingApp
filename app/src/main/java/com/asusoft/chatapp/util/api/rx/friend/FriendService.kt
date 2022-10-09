package com.asusoft.chatapp.util.api.rx.friend

import com.asusoft.chatapp.util.api.domain.friend.FriendCreateDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.RetrofitClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable

object FriendService {

    private val friendRepository: FriendRepository = RetrofitClient.getInstanceAPI("FriendRepository") as FriendRepository
    private val objectMapper = ObjectMapper()

    fun add(dto: FriendCreateDto): Observable<Long> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return friendRepository.add(map)
    }

    fun list(memberId: Long): Observable<List<MemberReadDto>> {
        return friendRepository.list(memberId)
    }

    fun remove(memberId: Long, friendId: Long): Observable<Long> {
        return friendRepository.remove(memberId, friendId)
    }

}