package com.asusoft.chatapp.activity.login

import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.chatting.HomeActivity
import com.asusoft.chatapp.databinding.ActivitySignUpBinding
import com.asusoft.chatapp.util.api.domain.member.MemberCreateDto
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.member.MemberService
import com.asusoft.chatapp.util.api.rx.profile.ProfileService
import com.soundcloud.android.crop.Crop
import com.soundcloud.android.crop.CropImageActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var resultPickerLauncher: ActivityResultLauncher<Intent>

    private var destination: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.signup_text)

        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != RESULT_OK) return@registerForActivityResult

                val source = it.data?.data ?: return@registerForActivityResult
                try {
                    destination = Uri.fromFile(File(cacheDir, "cropped"))
                    val crop = Crop.of(source, destination).asSquare()
                    resultPickerLauncher.launch(crop.getIntent(baseContext))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        resultPickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != RESULT_OK) return@registerForActivityResult
                if (destination == null) return@registerForActivityResult

                val source = ImageDecoder.createSource(contentResolver, destination!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                binding.iv.setImageBitmap(bitmap)
            }

        binding.btnSignUp.setOnClickListener {
            val name = binding.tvName.text.toString()
            val id = binding.tvId.text.toString()
            val pw = binding.tvPw.text.toString()

            val dto = MemberCreateDto(name, id, pw)
            signUp(dto)
        }

        binding.card.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }
    }

    private fun signUp(dto: MemberCreateDto) {
        val api = MemberService.signUp(dto)

        ApiController.apiSubscribe(
            api,
            this,
            { result ->
                if (result !is Long) return@apiSubscribe

                ApiController.toast(this, "회원가입 되었습니다.")
                if (destination == null) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    profileUpload(result)
                }
            }, {
                ApiController.toast(this, "이미 가입한 회원의 아이디 또는 닉네임입니다.")
            }
        )
    }

    private fun profileUpload(memberId: Long) {
        val inputStream = contentResolver.openInputStream(destination!!)

        if (inputStream == null) {
            setResult(RESULT_OK)
            finish()
            return
        }

        val part = MultipartBody.Part.createFormData(
            "file", "myPic", RequestBody.create(
                MediaType.parse("image/*"),
                inputStream.readBytes()
            )
        )

        val api = ProfileService.upload(memberId, part)

        ApiController.apiSubscribe(
            api,
            this,
            { _ ->
                ApiController.toast(this, "프로필 업로드 성공")
                setResult(RESULT_OK)
                finish()
            }, {
                ApiController.toast(this, "프로필 업로드 실패")
            }
        )
    }
}