package com.asusoft.chatapp

import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.asusoft.chatapp.api.rx.ApiClient
import com.asusoft.chatapp.api.rx.MemberApiClient
import com.asusoft.chatapp.api.rx.RxBus
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.Callable


class ParsingTest {

    @Before
    fun before() {
        RxJavaPlugins.setIoSchedulerHandler {
            Schedulers.trampoline()
        }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler {
            Schedulers.trampoline()
        }
    }

    @After
    fun teardown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

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

    @Test
    fun 회원가입() {
        registerRxBus()
        signUp()

        Thread.sleep(5000)
    }


    private fun signUp() {
        val dto = CreateMemberDto("Asu", "asukim2020", "1234")

        println("call")
        val api = MemberApiClient.signUp(dto)
        ApiClient.buildDisposable(api, RxBus.SIGN_UP).apply {
            println("apply")
        }
    }

    private fun registerRxBus() {
        RxBus.receiveEvent(RxBus.SIGN_UP).subscribe {
            println("call")
            val responseBody = it as? ResponseBody ?: return@subscribe

            println(responseBody)
        }.apply {
            println("apply")
        }
    }


}