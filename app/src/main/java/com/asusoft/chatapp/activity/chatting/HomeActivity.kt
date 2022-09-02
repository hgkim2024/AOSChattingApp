package com.asusoft.chatapp.activity.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.asusoft.chatapp.R
import com.asusoft.chatapp.databinding.ActivityHomeBinding
import com.asusoft.chatapp.fragment.ChatRoomFragment
import com.asusoft.chatapp.fragment.FriendFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var friendFragment: FriendFragment
    private lateinit var chatRoomFragment: ChatRoomFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendFragment = FriendFragment()
        chatRoomFragment = ChatRoomFragment()

        binding.bottomTabBar.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.person -> setCurrentFragment(friendFragment)
                R.id.bubble -> setCurrentFragment(chatRoomFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }
}