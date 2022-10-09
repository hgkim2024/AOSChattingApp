package com.asusoft.chatapp.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.chatting.ChattingActivity
import com.asusoft.chatapp.databinding.ActivityProfileBinding
import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomCreateDto
import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomReadDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.chatroom.ChatRoomService
import com.asusoft.chatapp.util.extension.imageLoad
import com.asusoft.chatapp.util.extension.onClick

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var myInfo: MemberReadDto
    private lateinit var clickInfo: MemberReadDto

    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        myInfo = intent.getSerializableExtra("myInfo") as MemberReadDto
        clickInfo = intent.getSerializableExtra("clickInfo") as MemberReadDto

        if(myInfo.id != clickInfo.id) {
            binding.ivBtnCenter.setImageDrawable(ContextCompat.getDrawable(baseContext, R.drawable.ic_chat_bubble_24))
            binding.tvBtnCenter.text = "1:1 채팅"
        }

        binding.iv.imageLoad(this, clickInfo.profileUrl, R.drawable.ic_person_24)
        binding.tv.text = clickInfo.name

        binding.iv.onClick {
            val intent = Intent(this, ProfilePullScreenActivity::class.java)
            intent.putExtra("myInfo", clickInfo)
            resultLauncher.launch(intent)
        }

        binding.clear.onClick {
            exit()
        }

        binding.btnCenter.onClick {
            if (myInfo.id == clickInfo.id) {
                val intent = Intent(this, ProfileEditActivity::class.java)
                intent.putExtra("myInfo", myInfo)
                resultLauncher.launch(intent)
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_in)
            } else {
                enterChatRoom(clickInfo)
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val intent = it.data ?: return@registerForActivityResult
                myInfo = intent.getSerializableExtra("myInfo") as? MemberReadDto ?: return@registerForActivityResult

                binding.iv.imageLoad(this, myInfo.profileUrl, R.drawable.ic_person_24)
                isEdit = true
            }
        }
    }


    private fun createChatRoom(friend: MemberReadDto) {
        myInfo.id ?: return
        friend.id ?: return

        val dto = ChatRoomCreateDto("", myInfo.id!!, friend.id!!)
        val api = ChatRoomService.create(dto)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                val chatRoom = result as? ChatRoomReadDto ?: return@apiSubscribe
                startChattingActivity(friend, chatRoom)
            }, {
                ApiController.toast(baseContext, "친구목록 불러오기 실패")
            }
        )
    }

    private fun enterChatRoom(friend: MemberReadDto) {
        myInfo.id ?: return

        val api = ChatRoomService.list(myInfo.id!!)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                val chatRoomList = result as? ArrayList<ChatRoomReadDto> ?: return@apiSubscribe
                val chatRoom = isChatRoom(friend, chatRoomList)
                if(chatRoom == null) {
                    createChatRoom(friend)
                } else {
                    startChattingActivity(friend, chatRoom)
                }
            }, {
                ApiController.toast(baseContext, "친구목록 불러오기 실패")
            }
        )
    }

    private fun startChattingActivity(
        friend: MemberReadDto,
        chatRoom: ChatRoomReadDto
    ) {
        val intent = Intent(baseContext, ChattingActivity::class.java)
        intent.putExtra("chatRoom", chatRoom)
        intent.putExtra("myInfo", myInfo)
        intent.putExtra("friendInfo", friend)

        resultLauncher.launch(intent)
    }

    private fun isChatRoom(
        friend: MemberReadDto,
        chatRoomList:List<ChatRoomReadDto>
    ): ChatRoomReadDto? {
        chatRoomList.forEach {
            if (friend.id == it.getFriend(myInfo)?.id) {
                return it
            }
        }

        return null
    }

    override fun onBackPressed() {
        exit()
    }

    private fun exit() {
        if (isEdit) {
            val intent = Intent()
            intent.putExtra("myInfo", myInfo)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}

