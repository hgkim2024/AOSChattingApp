package com.asusoft.chatapp.util.api.rx.profile

import com.asusoft.chatapp.util.api.rx.RetrofitClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.reactivex.Observable
import okhttp3.MultipartBody
import java.io.InputStream

object ProfileService {

    private val profileRepository: ProfileRepository = RetrofitClient.getInstanceAPI("ProfileRepository") as ProfileRepository
//    private val objectMapper = ObjectMapper()

    public fun upload(
        memberId: Long,
        part: MultipartBody.Part
    ): Observable<Long> {
        return profileRepository.upload(memberId, part)
    }

}