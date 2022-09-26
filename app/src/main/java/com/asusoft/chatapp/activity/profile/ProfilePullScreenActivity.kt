package com.asusoft.chatapp.activity.profile

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.asusoft.chatapp.R
import com.asusoft.chatapp.application.ChattingApplication
import com.asusoft.chatapp.databinding.ActivityProfilePullScreenBinding
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.extension.imageLoad
import com.asusoft.chatapp.util.extension.onClick
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class ProfilePullScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePullScreenBinding

    private lateinit var myInfo: MemberReadDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePullScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        myInfo = intent.getSerializableExtra("myInfo") as MemberReadDto
        binding.iv.imageLoad(this, myInfo.profileUrl, R.drawable.ic_person_24)

        if (myInfo.profileUrl == null) {
            binding.iv.setBackgroundColor(Color.parseColor("#FF94bfe7"))
        } else {
            binding.iv.setBackgroundColor(Color.BLACK)
        }

        binding.clear.onClick {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}