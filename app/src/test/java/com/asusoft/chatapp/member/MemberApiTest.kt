package com.asusoft.chatapp.member

import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.rx.ApiClient
import com.asusoft.chatapp.api.rx.member.MemberApiClient
import com.asusoft.chatapp.api.rx.RxBus
import com.asusoft.chatapp.api.rx.member.MemberApi
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test

class MemberApiTest {
    private val memberApi = MemberApi()

    private val NAME = "Asu"
    private val ID = "asukim2020"
    private val PW = "1234"

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
    fun after() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
        memberApi.dispose()
    }

    @Test
    fun 회원가입() {
        val dto = CreateMemberDto(NAME, ID, PW)
        memberApi.signUp(dto)
        Thread.sleep(5000)
    }

    @Test
    fun 로그인() {
        val dto = LoginDto(ID, PW)
        memberApi.login(dto)
        Thread.sleep(5000)
    }

}