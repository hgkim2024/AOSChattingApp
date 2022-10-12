package com.asusoft.chatapp.activity.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusoft.chatapp.databinding.ActivityAddFriendBinding
import com.asusoft.chatapp.databinding.ActivityChattingBinding
import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomReadDto
import com.asusoft.chatapp.util.api.domain.chtting.ChattingCreateDto
import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.chtting.ChattingService
import com.asusoft.chatapp.util.api.rx.friend.FriendService
import com.asusoft.chatapp.util.extension.onClick
import com.asusoft.chatapp.util.recyclerview.RecyclerItemClickListener
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter
import com.jakewharton.rxbinding4.view.clicks

class ChattingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChattingBinding

    private lateinit var chatRoom: ChatRoomReadDto
    private lateinit var myInfo: MemberReadDto
    private lateinit var friendInfo: MemberReadDto

    private lateinit var adapter: RecyclerViewAdapter
    private var chattingList: ArrayList<Any> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chatRoom = intent.getSerializableExtra("chatRoom") as ChatRoomReadDto
        myInfo = intent.getSerializableExtra("myInfo") as MemberReadDto
        friendInfo = intent.getSerializableExtra("friendInfo") as MemberReadDto

        updateChattingList()

        adapter = RecyclerViewAdapter(this, chattingList, myInfo, friendInfo)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(baseContext)

        binding.recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                baseContext,
                binding.recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {

                    }

                    override fun onItemLongClick(view: View?, position: Int) {}
                }
            )
        )

        binding.btnSend.onClick {
            val message = binding.tv.text.toString()
            sendChatting(message)
            binding.tv.setText("")
        }
    }

    private fun updateChattingList() {
        chatRoom.id ?: return

        val api = ChattingService.list(chatRoom.id!!)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                chattingList = result as? ArrayList<Any> ?: return@apiSubscribe
                adapter.list = chattingList
                adapter.notifyDataSetChanged()
            }, {
                ApiController.toast(this, "친구 닉네임을 찾을 수 없거나 이미 친구입니다.")
            }
        )
    }

    private fun sendChatting(message: String) {
        val dto = ChattingCreateDto(message, myInfo.id, chatRoom.id)
        val api = ChattingService.create(dto)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                if (result !is Long) return@apiSubscribe

            }, {
                ApiController.toast(this, "친구 닉네임을 찾을 수 없거나 이미 친구입니다.")
            }
        )
    }
}