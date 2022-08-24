package com.asusoft.chatapp.activity.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.asusoft.chatapp.R
import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.asusoft.chatapp.api.rx.member.MemberApi
import com.asusoft.chatapp.api.status.StatusCode
import com.asusoft.chatapp.databinding.ActivitySignUpBinding
import com.asusoft.chatapp.eventbus.BusMap
import com.asusoft.chatapp.eventbus.BusMap.*
import com.asusoft.chatapp.eventbus.GlobalBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var memberApi: MemberApi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.signup_text)

        binding.btnSignUp.setOnClickListener {
            val name = binding.tvNickName.text.toString()
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()

            val dto = CreateMemberDto(name, id, pw)
            memberApi?.signUp(dto)
        }
    }

    override fun onStart() {
        super.onStart()
        val memberApi = MemberApi()
        this.memberApi = memberApi
        memberApi.setContext(this)
        GlobalBus.register(this)
    }

    override fun onStop() {
        memberApi?.dispose()
        memberApi = null
        GlobalBus.unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(map: HashMap<String, Any>) {
        val title = map[TITLE.value] ?: return
        val status = map[STATUS.value] ?: return
//        val result = map[RESULT.value] ?: return

        if(
            title == MemberApi.toString()
            && status == StatusCode.OK
        ) {
            Toast.makeText(this, "회원가입 되었습니다.", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        }
    }
}