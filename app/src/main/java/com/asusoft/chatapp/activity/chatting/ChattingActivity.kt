package com.asusoft.chatapp.activity.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusoft.chatapp.databinding.ActivityAddFriendBinding
import com.asusoft.chatapp.databinding.ActivityChattingBinding
import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomReadDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.recyclerview.RecyclerItemClickListener
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter

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

        updateChatList()

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
    }

    private fun updateChatList() {
        // TODO: - api 연동
    }
}