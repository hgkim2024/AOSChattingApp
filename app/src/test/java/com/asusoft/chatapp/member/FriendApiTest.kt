package com.asusoft.chatapp.member

import androidx.appcompat.app.AppCompatActivity
import com.asusoft.chatapp.api.domain.friend.FriendCreateDto
import com.asusoft.chatapp.api.domain.member.MemberReadDto
import com.asusoft.chatapp.api.rx.ApiController
import com.asusoft.chatapp.api.rx.friend.FriendService
import com.asusoft.chatapp.api.rx.member.MemberService
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test

class FriendApiTest {

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
    }

    @Test
    fun 친구추가() {

        val dto = FriendCreateDto(1L, 2L)
        val api = FriendService.addFriend(dto)

        ApiController.apiSubscribe(
            api,
            null,
            { result ->
                if (result !is Long) return@apiSubscribe
                println("======================")
                println("친구추가 성공")
                println("========================")
            }, {
                println("======================")
                println("친구추가 실패")
                println("========================")
            }
        )

        Thread.sleep(5000)
    }

    @Test
    fun 친구리스트() {
        val api = FriendService.getFriendList(1L)

        ApiController.apiSubscribe(
            api,
            null,
            { result ->
                if (result !is List<*>) return@apiSubscribe
                val memberList = result as List<MemberReadDto>
                println("======================")
                println(memberList)
                println("========================")
            }, {
                println("======================")
                println("리스트 가져오기 실패")
                println("========================")
            }
        )

        Thread.sleep(5000)
    }

    @Test
    fun 친구삭제() {
        val api = FriendService.removeFriend(1L, 2L)

        ApiController.apiSubscribe(
            api,
            null,
            { result ->
                if (result !is Long) return@apiSubscribe
                println("======================")
                println("친구삭제 성공")
                println("========================")
            }, {
                println("======================")
                println("친구삭제 실패")
                println(it)
                println("========================")
            }
        )

        Thread.sleep(5000)
    }

}