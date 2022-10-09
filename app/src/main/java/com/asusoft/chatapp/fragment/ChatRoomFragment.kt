package com.asusoft.chatapp.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.profile.ProfileActivity
import com.asusoft.chatapp.databinding.FragmentChatRoomBinding
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.chatroom.ChatRoomService
import com.asusoft.chatapp.util.recyclerview.RecyclerItemClickListener
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter

class ChatRoomFragment : Fragment() {

    private lateinit var myInfo: MemberReadDto
    private var chatRoomList: ArrayList<Any> = ArrayList()

    private lateinit var binding: FragmentChatRoomBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myInfo = it.getSerializable("myInfo") as MemberReadDto
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
            }, {
                ApiController.toast(context, "친구목록 불러오기 실패")
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(myInfo: MemberReadDto) =
            ChatRoomFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("myInfo", myInfo)
                }
            }
    }
}