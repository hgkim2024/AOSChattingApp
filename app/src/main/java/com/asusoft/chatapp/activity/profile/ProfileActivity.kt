package com.asusoft.chatapp.activity.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.login.SignUpActivity
import com.asusoft.chatapp.application.ChattingApplication
import com.asusoft.chatapp.databinding.ActivityProfileBinding
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.extension.imageLoad
import com.asusoft.chatapp.util.extension.onClick
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var myInfo: MemberReadDto
    private lateinit var clickInfo: MemberReadDto

    private var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        myInfo = intent.getSerializableExtra("myInfo") as MemberReadDto
        clickInfo = intent.getSerializableExtra("clickInfo") as MemberReadDto

        binding.iv.imageLoad(this, clickInfo.profileUrl, R.drawable.ic_person_24)
        binding.tv.text = clickInfo.name

        binding.iv.onClick {
            val intent = Intent(this, ProfilePullScreenActivity::class.java)
            intent.putExtra("myInfo", clickInfo)
            resultLauncher.launch(intent)
        }

        binding.clear.onClick {
            exit()
        }

        binding.btnCenter.onClick {
            if (myInfo.id == clickInfo.id) {
                val intent = Intent(this, ProfileEditActivity::class.java)
                intent.putExtra("myInfo", myInfo)
                resultLauncher.launch(intent)
                overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_in)
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val intent = it.data ?: return@registerForActivityResult
                myInfo = intent.getSerializableExtra("myInfo") as? MemberReadDto ?: return@registerForActivityResult

                binding.iv.imageLoad(this, myInfo.profileUrl, R.drawable.ic_person_24)
                isEdit = true
            }
        }
    }

    override fun onBackPressed() {
        exit()
    }

    private fun exit() {
        if (isEdit) {
            val intent = Intent()
            intent.putExtra("myInfo", myInfo)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}

