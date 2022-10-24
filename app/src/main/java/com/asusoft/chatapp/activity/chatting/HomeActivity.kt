package com.asusoft.chatapp.activity.chatting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.asusoft.chatapp.R
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.databinding.ActivityHomeBinding
import com.asusoft.chatapp.fragment.ChatRoomFragment
import com.asusoft.chatapp.fragment.FriendFragment
import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var chattingReadDto: ChattingReadDto? = null

    private lateinit var friendFragment: FriendFragment
    private lateinit var chatRoomFragment: ChatRoomFragment
    private lateinit var myInfo: MemberReadDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myInfo = intent.getSerializableExtra("myInfo") as MemberReadDto
        chattingReadDto = intent.getSerializableExtra("chattingReadDto") as? ChattingReadDto

        friendFragment = FriendFragment.newInstance(myInfo)
        chatRoomFragment = ChatRoomFragment.newInstance(myInfo, chattingReadDto)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //TODO: - 친구 추가 후 친구목록 갱신
//            if (it.resultCode == RESULT_OK) {
//
//            }
        }

//        supportActionBar?.title = getString(R.string.login_text)

        binding.bottomTabBar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.person -> {
                    setCurrentFragment(friendFragment)
                }
                R.id.bubble -> setCurrentFragment(chatRoomFragment)
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        if (chattingReadDto != null) {
            setCurrentFragment(chatRoomFragment)
            chattingReadDto = null
        } else {
            setCurrentFragment(friendFragment)
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {

        if (fragment == chatRoomFragment) {
            supportActionBar?.title = "채팅"
        } else if (fragment == friendFragment) {
            supportActionBar?.title = "친구"
       }

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_side, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addFriend -> {
                val intent = Intent(this, AddFriendActivity::class.java)
                intent.putExtra("myInfo", myInfo)
                resultLauncher.launch(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}