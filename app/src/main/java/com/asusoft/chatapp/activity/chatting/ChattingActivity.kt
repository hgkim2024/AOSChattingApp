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
import com.asusoft.chatapp.util.eventbus.GlobalBus
import com.asusoft.chatapp.util.extension.onClick
import com.asusoft.chatapp.util.fcm.FCMService
import com.asusoft.chatapp.util.recyclerview.RecyclerItemClickListener
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter
import com.jakewharton.rxbinding4.view.clicks
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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

    override fun onStart() {
        super.onStart()
        if (chatRoom.id != null) {
            chatroomId = chatRoom.id!!
        }
        updateChattingList()
        GlobalBus.register(this)
    }

    override fun onStop() {
        chatroomId = -1
        GlobalBus.unregister(this)
        super.onStop()
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
        val dto = ChattingCreateDto(message, myInfo.id, friendInfo.id, chatRoom.id)
        val api = ChattingService.create(dto)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                if (result !is Long) return@apiSubscribe
                val chattingReadDto = ChattingReadDto(result, dto.message, myInfo.id, chatRoom.id)
                addChatting(chattingReadDto)
            }, {
                ApiController.toast(this, "친구 닉네임을 찾을 수 없거나 이미 친구입니다.")
            }
        )
    }

    override fun onDestroy() {
        chatroomId = -1
        super.onDestroy()
    }

    private fun addChatting(dto: ChattingReadDto) {
        adapter.list.add(dto)
        adapter.notifyItemInserted(adapter.list.size)
        binding.recyclerView.scrollToPosition(adapter.list.size - 1)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onEvent(map: HashMap<String, Any>) {
        if (map[FCMService.toString()] != null) {
            val dto = map["dto"] as? ChattingReadDto ?: return
            addChatting(dto)
        }
    }

    companion object {
        var chatroomId: Long = -1
    }
}