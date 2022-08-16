package com.asusoft.chatapp.api.rx.member

import android.app.Activity
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.asusoft.chatapp.R
import com.asusoft.chatapp.activity.StartActivity
import com.asusoft.chatapp.api.domain.member.CreateMemberDto
import com.asusoft.chatapp.api.domain.member.LoginDto
import com.asusoft.chatapp.api.domain.member.ReadMemberDto
import com.asusoft.chatapp.api.rx.ApiClient
import com.asusoft.chatapp.api.rx.RxBus
import com.asusoft.chatapp.api.status.StatusCode.*
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import retrofit2.HttpException

class MemberApi {

    private var context: Context? = null

    private val disposables by lazy {
        CompositeDisposable()
    }

    init {
        registerSuccessRxBus()
        registerFailRxBus()
    }

    fun setContext(ctx: Context) {
        context = ctx
    }

    fun dispose() {
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
        context = null
    }

    fun signUp(dto: CreateMemberDto) {
        val api = MemberApiClient.signUp(dto)
        ApiClient.buildDisposable(api, RxBus.SIGN_UP, RxBus.SIGN_UP_ERROR).apply { disposables.add(this) }
    }

    fun login(dto: LoginDto) {
        val api = MemberApiClient.login(dto)
        ApiClient.buildDisposable(api, RxBus.LOGIN, RxBus.LOGIN_ERROR).apply { disposables.add(this) }
    }

    private fun registerSuccessRxBus() {
        RxBus.receiveEvent(RxBus.SIGN_UP).subscribe {
            val responseBody = it as? ResponseBody ?: return@subscribe

            toastMsg("회원가입 성공 $responseBody")
        }.apply { disposables.add(this) }

        RxBus.receiveEvent(RxBus.LOGIN).subscribe {
            val dto = it as? ReadMemberDto ?: return@subscribe

            toastMsg("로그인 성공 $dto")
//            (context as StartActivity).binding.tvId.setText("1234")
//            (context as Activity).findViewById<EditText>(R.id.tvId).setText("1234")
        }.apply { disposables.add(this) }
    }

    private fun registerFailRxBus() {
        RxBus.receiveEvent(RxBus.SIGN_UP_ERROR).subscribe {
            val error = it as? HttpException ?: return@subscribe
            when(error.code()) {
                NotFound.code -> {
                    toastMsg("회원가입 실패")
                }
            }
        }.apply { disposables.add(this) }

        RxBus.receiveEvent(RxBus.LOGIN_ERROR).subscribe {
            val error = it as? HttpException ?: return@subscribe
            when(error.code()) {
                NotFound.code -> {
                    toastMsg("로그인 실패")
                }
            }
        }.apply { disposables.add(this) }
    }

    private fun toastMsg(msg: String) {
        val ctx = context ?: return
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }
}