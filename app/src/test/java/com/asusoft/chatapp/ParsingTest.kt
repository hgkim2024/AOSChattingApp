package com.asusoft.chatapp

import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test


class ParsingTest {

    @Test
    fun parsing() {
        val objectMapper = ObjectMapper()
        val dto = CreateMemberDto("Asu", "asukim2020", "1234")
        val map = objectMapper.convertValue(dto, Map::class.java) as Map<String, Any>

        for (entry in map) {
            println(entry.key)
            println(entry.value)
            println(entry.key.javaClass.name)
            println(entry.value.javaClass.name)
        }
    }

}