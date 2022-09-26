package com.asusoft.chatapp.activity.profile

import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.asusoft.chatapp.R
import com.asusoft.chatapp.application.ChattingApplication
import com.asusoft.chatapp.databinding.ActivityProfileEditBinding
import com.asusoft.chatapp.util.api.domain.member.MemberReadDto
import com.asusoft.chatapp.util.api.rx.ApiController
import com.asusoft.chatapp.util.api.rx.profile.ProfileService
import com.asusoft.chatapp.util.extension.imageLoad
import com.asusoft.chatapp.util.extension.onClick
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.soundcloud.android.crop.Crop
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var resultPickerLauncher: ActivityResultLauncher<Intent>

    private lateinit var myInfo: MemberReadDto
    private var destination: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        myInfo = intent.getSerializableExtra("myInfo") as MemberReadDto
        binding.iv.imageLoad(this, myInfo.profileUrl, R.drawable.ic_person_24)
        binding.tv.text = myInfo.name

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

        binding.cancel.onClick {
            setResult(RESULT_CANCELED)
            finish()
        }

        binding.confirm.onClick {
            if (destination == null) {
                setResult(RESULT_CANCELED)
                finish()
            } else {
                profileUpload()
            }
        }

        binding.card.onClick {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }
    }

    private fun profileUpload() {
        val memberId = myInfo.id
        val inputStream = contentResolver.openInputStream(destination!!)

        if (inputStream == null || memberId == null) {
            setResult(RESULT_CANCELED)
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
            { result ->
                ApiController.toast(this, "프로필 업로드 성공")
                val intent = Intent()
                if (result is MemberReadDto) {
                    intent.putExtra("myInfo", result)
                }
                setResult(RESULT_OK, intent)
                finish()
            }, {
                ApiController.toast(this, "프로필 업로드 실패")
            }
        )
    }
}