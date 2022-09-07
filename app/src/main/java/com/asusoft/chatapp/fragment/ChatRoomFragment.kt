package com.asusoft.chatapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asusoft.chatapp.R
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto

class ChatRoomFragment : Fragment() {

    private lateinit var myInfo: MemberReadDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myInfo = it.getSerializable("myInfo") as MemberReadDto
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_room, container, false)
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