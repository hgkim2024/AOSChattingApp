package com.asusoft.chatapp.util.api.rx.profile

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*
import java.io.File

interface ProfileRepository {

    @Multipart
    @POST("member/file")
    fun upload(
        @Query("memberId") memberId: Long,
        @Part file: MultipartBody.Part
    ): Observable<Long>

}