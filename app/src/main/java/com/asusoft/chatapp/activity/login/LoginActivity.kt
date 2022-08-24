package com.asusoft.chatapp.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.asusoft.chatapp.R
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.rx.member.MemberApi
import com.asusoft.chatapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var memberApi: MemberApi? = null

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO:- 더이데이터 나중에 제거하기
        binding.tvId.setText("asukim2020")
        binding.tvPw.setText("1234")

        supportActionBar?.title = getString(R.string.login_text)

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Toast.makeText(this, "정상적으로 엑티비티를 종료하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()

            val dto = LoginDto(id, pw)
            memberApi?.login(dto)
        }

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            resultLauncher.launch(intent)
//            overridePendingTransition(androidx.appcompat.R.anim.abc_popup_enter, androidx.appcompat.R.anim.abc_fade_in)
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