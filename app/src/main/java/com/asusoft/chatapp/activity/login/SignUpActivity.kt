package com.asusoft.chatapp.activity.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asusoft.chatapp.R
import com.asusoft.chatapp.api.domain.member.MemberCreateDto
import com.asusoft.chatapp.api.rx.ApiController
import com.asusoft.chatapp.api.rx.member.MemberService
import com.asusoft.chatapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.signup_text)

        binding.btnSignUp.setOnClickListener {
            val name = binding.tvName.text.toString()
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()

            val dto = MemberCreateDto(name, id, pw)
            signUp(dto)
        }
    }

    private fun signUp(dto: MemberCreateDto) {
        val api = MemberService.signUp(dto)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                if (result !is Long) return@apiSubscribe

                ApiController.toast(this,"회원가입 되었습니다.")
                setResult(RESULT_OK)
                finish()
            }, {
                ApiController.toast(this, "이미 가입한 회원의 아이디 또는 닉네임입니다.")
            }
        )
    }
}