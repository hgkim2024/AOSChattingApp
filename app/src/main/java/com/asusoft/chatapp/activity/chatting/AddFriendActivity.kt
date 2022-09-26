package com.asusoft.chatapp.activity.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asusoft.chatapp.R
import com.asusoft.chatapp.application.ChattingApplication
import com.asusoft.chatapp.util.api.domain.friend.FriendCreateDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.friend.FriendService
import com.asusoft.chatapp.databinding.ActivityAddFriendBinding
import com.asusoft.chatapp.util.extension.onClick
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddFriendBinding

    private lateinit var myInfo: MemberReadDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myInfo = intent.getSerializableExtra("myInfo") as MemberReadDto

        supportActionBar?.title = getString(R.string.add_friend_title)

        binding.btnSend.onClick {
            val myId = myInfo.id
            val friendName = binding.tvName.text.toString()
            val dto = FriendCreateDto(myId, friendName)
            val api = FriendService.addFriend(dto)

            ApiController.apiSubscribe(
                api,
                this,
                { result ->
                    if (result !is Long) return@apiSubscribe
                    ApiController.toast(this, "${friendName}님과 친구가 되었습니다.")
                    setResult(RESULT_OK)
                    finish()
                }, {
                    ApiController.toast(this, "친구 닉네임을 찾을 수 없거나 이미 친구입니다.")
                }
            )
        }
    }
}