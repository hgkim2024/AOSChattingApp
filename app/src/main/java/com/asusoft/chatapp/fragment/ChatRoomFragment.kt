package com.asusoft.chatapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusoft.chatapp.activity.chatting.ChattingActivity
import com.asusoft.chatapp.databinding.FragmentChatRoomBinding
import com.asusoft.chatapp.util.api.domain.chatroom.ChatRoomReadDto
import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.chatroom.ChatRoomService
import com.asusoft.chatapp.util.recyclerview.RecyclerItemClickListener
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter

class ChatRoomFragment : Fragment() {

    private lateinit var myInfo: MemberReadDto
    private var chatRoomList: ArrayList<Any> = ArrayList()
    private var chattingReadDto: ChattingReadDto? = null

    private lateinit var binding: FragmentChatRoomBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myInfo = it.getSerializable("myInfo") as MemberReadDto
            chattingReadDto = it.getSerializable("chattingReadDto") as? ChattingReadDto
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//
//            }
        }

        updateChatRoomList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        adapter = RecyclerViewAdapter(this, chatRoomList, myInfo)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                binding.recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val chatRoom = adapter.list[position] as? ChatRoomReadDto ?: return
                        val intent = Intent(context, ChattingActivity::class.java)
                        intent.putExtra("chatRoom", chatRoom)
                        intent.putExtra("myInfo", myInfo)
                        intent.putExtra("friendInfo", chatRoom.getFriend(myInfo))

                        resultLauncher.launch(intent)
                    }

                    override fun onItemLongClick(view: View?, position: Int) {}
                }
            )
        )

        return binding.root
    }

    private fun updateChatRoomList() {
        myInfo.id ?: return

        val api = ChatRoomService.list(myInfo.id!!)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                chatRoomList = result as? ArrayList<Any> ?: return@apiSubscribe
                adapter.list = chatRoomList
                adapter.notifyDataSetChanged()

                if (chattingReadDto != null) {
                    val chatRoom = chattingReadDto?.chatRoomId ?: return@apiSubscribe
                    val list = chatRoomList.filter {
                        if (it is ChatRoomReadDto) {
                            it.id == chatRoom
                        } else {
                            false
                        }
                    }
                    goChatroom(list[0] as? ChatRoomReadDto)
                }

            }, {
                ApiController.toast(context, "친구목록 불러오기 실패")
            }
        )
    }

    private fun goChatroom(chatRoom: ChatRoomReadDto?) {
        if (chatRoom == null) return
        val intent = Intent(context, ChattingActivity::class.java)
        intent.putExtra("chatRoom", chatRoom)
        intent.putExtra("myInfo", myInfo)
        intent.putExtra("friendInfo", chatRoom.getFriend(myInfo))

        resultLauncher.launch(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(myInfo: MemberReadDto, dto: ChattingReadDto? = null) =
            ChatRoomFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("myInfo", myInfo)
                    putSerializable("chattingReadDto", dto)
                }
            }
    }
}