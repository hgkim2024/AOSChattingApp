package com.asusoft.chatapp.util.api.rx.chtting

import com.asusoft.chatapp.util.api.domain.chtting.ChattingCreateDto
import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto
import com.asusoft.chatapp.util.api.rx.RetrofitClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable

object ChattingService {

    private val chattingRepository: ChattingRepository = RetrofitClient.getInstanceAPI("ChattingRepository") as ChattingRepository
    private val objectMapper = ObjectMapper()

    fun create(dto: ChattingCreateDto): Observable<ChattingReadDto> {
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, String>
        return chattingRepository.create(map)
    }

    fun list(chatroomId: Long): Observable<List<ChattingReadDto>> {
        return chattingRepository.list(chatroomId)
    }

}