package com.asusoft.chatapp.activity.login

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.chatting.HomeActivity
import com.asusoft.chatapp.util.api.domain.member.LoginDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.member.MemberService
import com.asusoft.chatapp.databinding.ActivityLoginBinding
import com.asusoft.chatapp.util.api.domain.chtting.ChattingReadDto
import com.asusoft.chatapp.util.extension.onClick
import com.asusoft.chatapp.util.objects.PreferenceKey
import com.asusoft.chatapp.util.objects.PreferenceManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var chattingReadDto: ChattingReadDto? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chattingReadDto = intent.getSerializableExtra("chattingReadDto") as? ChattingReadDto

        // TODO:- 더이데이터 나중에 제거하기
//        binding.tvId.setText("asukim2020")
//        binding.tvPw.setText("1234")

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
            intent.putExtra("chattingReadDto", chattingReadDto)
            resultLauncher.launch(intent)
//            overridePendingTransition(androidx.appcompat.R.anim.abc_popup_enter, androidx.appcompat.R.anim.abc_fade_in)
        }

        val id = PreferenceManager.getString(PreferenceKey.ID)
        val pw = PreferenceManager.getString(PreferenceKey.PW)

        if (id != "" && pw != "") {
            val dto = LoginDto(id, pw)
            login(dto)
        }
    }

    private fun login(dto: LoginDto) {
        val api = MemberService.login(dto)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                val memberReadDto: MemberReadDto = (result as? MemberReadDto) ?: return@apiSubscribe
//                Toast.makeText(this, dto.name, Toast.LENGTH_SHORT).show()
                ApiController.toast(this, memberReadDto.name)

                myInfo = memberReadDto
                getToken()

                if (binding.checkbox.isChecked) {
                    PreferenceManager.setString(PreferenceKey.ID, dto.id)
                    PreferenceManager.setString(PreferenceKey.PW, dto.pw)
                } else {
                    PreferenceManager.setString(PreferenceKey.ID, null)
                    PreferenceManager.setString(PreferenceKey.PW, null)
                }

                val intent = Intent(baseContext, HomeActivity::class.java)
                intent.putExtra("myInfo", memberReadDto)
                intent.putExtra("chattingReadDto", chattingReadDto)

                startActivity(intent)
                finish()

            }, {
                // TODO: - 예외처리 공통로직 만들것
                // 발견한 예외 HttpException, java.net.SocketTimeoutException
                ApiController.toast(this, "아이디 또는 비밀번호가 일치하지않습니다.")
            }
        )

    }

    private fun uploadFcmToken(memberId: Long, fcmToken: String) {
        val api = MemberService.uploadFcmToken(memberId, fcmToken)

        ApiController.apiSubscribe(
            api,
            this,
            { _ ->

            }, {
                ApiController.toast(this, "FCM 토큰 발행 실패")
            }
        )
    }

    private fun getToken(): String?{
        var token: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if(!task.isSuccessful){
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)

                return@OnCompleteListener
            }
            // get new FCM registration token
            token = task.result

            if (myInfo?.id != null && token != null) {
                uploadFcmToken(myInfo!!.id!!, token!!)
            }

            Log.d(TAG, "FCM token: $token")
        })

        return token
    }

    companion object {
        private const val TAG = "LoginActivity"
        public var myInfo: MemberReadDto? = null
    }
}