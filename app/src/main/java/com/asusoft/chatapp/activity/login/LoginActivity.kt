package com.asusoft.chatapp.activity.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.chatting.HomeActivity
import com.asusoft.chatapp.application.ChattingApplication
import com.asusoft.chatapp.util.api.domain.member.LoginDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.member.MemberService
import com.asusoft.chatapp.databinding.ActivityLoginBinding
import com.asusoft.chatapp.util.extension.onClick
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
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
//            if (it.resultCode == RESULT_OK) {
//
//            }
        }

        binding.btnLogin.onClick {
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()

            val dto = LoginDto(id, pw)
            login(dto)
        }

        binding.btnSignUp.onClick {
            val intent = Intent(this, SignUpActivity::class.java)
            resultLauncher.launch(intent)
//            overridePendingTransition(androidx.appcompat.R.anim.abc_popup_enter, androidx.appcompat.R.anim.abc_fade_in)
        }
    }

    private fun login(dto: LoginDto) {
        val api = MemberService.login(dto)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                val dto: MemberReadDto = (result as? MemberReadDto) ?: return@apiSubscribe
//                Toast.makeText(this, dto.name, Toast.LENGTH_SHORT).show()
                ApiController.toast(this, dto.name)

                val intent = Intent(baseContext, HomeActivity::class.java)
                intent.putExtra("myInfo", dto)
                startActivity(intent)
                finish()

            }, {
                // TODO: - 예외처리 공통로직 만들것
                // 발견한 예외 HttpException, java.net.SocketTimeoutException
                ApiController.toast(this, "아이디 또는 비밀번호가 일치하지않습니다.")
            }
        )

    }
}