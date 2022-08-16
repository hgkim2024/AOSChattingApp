package com.asusoft.chatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asusoft.chatapp.R
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.rx.member.MemberApi
import com.asusoft.chatapp.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartBinding
    private val memberApi = MemberApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        memberApi.setContext(this)

        binding.btnLogin.setOnClickListener {
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()

            val dto = LoginDto(id, pw)
            memberApi.login(dto)
        }
    }

    override fun onDestroy() {
        memberApi.dispose()
        super.onDestroy()
    }
}