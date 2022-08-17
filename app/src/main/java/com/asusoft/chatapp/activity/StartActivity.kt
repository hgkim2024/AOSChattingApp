package com.asusoft.chatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asusoft.chatapp.R
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.rx.member.MemberApi
import com.asusoft.chatapp.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private var memberApi: MemberApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()

            val dto = LoginDto(id, pw)
            memberApi?.login(dto)
        }
    }

    override fun onStart() {
        super.onStart()

        val memberApi = MemberApi()
        this.memberApi = memberApi
        memberApi.setContext(this)
    }

    override fun onStop() {
        memberApi?.dispose()
        memberApi = null
        super.onStop()
    }
}