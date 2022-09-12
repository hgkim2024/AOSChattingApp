package com.asusoft.chatapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusoft.chatapp.R
import com.asusoft.chatapp.databinding.FragmentFriendBinding
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.friend.FriendService
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter

class FriendFragment : Fragment() {

    private lateinit var myInfo: MemberReadDto
    private var friendList: List<MemberReadDto> = ArrayList()

    private lateinit var binding: FragmentFriendBinding
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myInfo = it.getSerializable("myInfo") as MemberReadDto
        }

        updateFriendList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendBinding.inflate(inflater, container, false)

        adapter = RecyclerViewAdapter(this, friendList as ArrayList<Any>)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    private fun updateFriendList() {
        myInfo ?: return
        if (myInfo.id != null) {
            val api = FriendService.getFriendList(myInfo.id!!)

            ApiController.apiSubscribe(
                api,
                this,
                { result ->
                    friendList = result as? List<MemberReadDto> ?: return@apiSubscribe
                    adapter.list = friendList as ArrayList<Any>
                    adapter.notifyDataSetChanged()
                }, {
                    ApiController.toast(context, "친구목록 불러오기 실패")
                }
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(myInfo: MemberReadDto) =
            FriendFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("myInfo", myInfo)
                }
            }
    }
}