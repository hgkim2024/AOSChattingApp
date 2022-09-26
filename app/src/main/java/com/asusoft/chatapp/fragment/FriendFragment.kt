package com.asusoft.chatapp.fragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.login.SignUpActivity
import com.asusoft.chatapp.activity.profile.ProfileActivity
import com.asusoft.chatapp.databinding.FragmentFriendBinding
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.friend.FriendService
import com.asusoft.chatapp.util.extension.imageLoad
import com.asusoft.chatapp.util.recyclerview.RecyclerItemClickListener
import com.asusoft.chatapp.util.recyclerview.RecyclerViewAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class FriendFragment : Fragment() {

    private lateinit var myInfo: MemberReadDto
    private var friendList: ArrayList<Any> = ArrayList()

    private lateinit var binding: FragmentFriendBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            myInfo = it.getSerializable("myInfo") as MemberReadDto
        }

        updateFriendList()

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val intent = it.data ?: return@registerForActivityResult
                myInfo = intent.getSerializableExtra("myInfo") as? MemberReadDto ?: return@registerForActivityResult
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendBinding.inflate(inflater, container, false)

        adapter = RecyclerViewAdapter(this, friendList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)

        binding.recyclerView.addOnItemTouchListener(
            RecyclerItemClickListener(
                context,
                binding.recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        if (adapter.list[position] is MemberReadDto) {
                            val intent = Intent(requireContext(), ProfileActivity::class.java)
                            intent.putExtra("myInfo", myInfo)
                            intent.putExtra("clickInfo", adapter.list[position] as MemberReadDto)
                            resultLauncher.launch(intent)
                        }
                    }

                    override fun onItemLongClick(view: View?, position: Int) {}
                }
            )
        )

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
                    friendList = result as? ArrayList<Any> ?: return@apiSubscribe
                    friendList.add(0, "친구 ${friendList.size}")
                    friendList.add(0, myInfo)
                    adapter.list = friendList
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