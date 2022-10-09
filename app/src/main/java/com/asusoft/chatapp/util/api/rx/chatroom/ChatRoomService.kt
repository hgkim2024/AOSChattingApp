package com.asusoft.chatapp.util.api.rx.chatroom

import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomCreateDto
import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomReadDto
import com.asusoft.chatapp.util.api.domain.chatroom.EntryReadDto
import com.asusoft.chatapp.util.api.rx.RetrofitClient
import com.asusoft.chatapp.util.api.rx.friend.FriendRepository
import com.asusoft.chatapp.util.api.rx.friend.FriendService
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable

object ChatRoomService {

    private val chatRoomRepository: ChatRoomRepository = RetrofitClient.getInstanceAPI("ChatRoomRepository") as ChatRoomRepository
    private val objectMapper = ObjectMapper()

    fun create(dto: ChatRoomCreateDto): Observable<ChatRoomReadDto> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return chatRoomRepository.create(map)
    }

    fun list(memberId: Long): Observable<List<ChatRoomReadDto>> {
        return chatRoomRepository.list(memberId)
    }
}