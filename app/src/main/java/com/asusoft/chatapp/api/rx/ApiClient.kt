package com.asusoft.chatapp.api.rx

import android.util.Log
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response

object ApiClient {

    val url = "http://192.168.0.16:8080/"
//    val url = "http://3.37.113.193:8080/"

    fun <T> buildDisposable(
        any: Maybe<Response<T>>,
        rxBusKey: String,
        retryCount: Int = 1
    ): Disposable {
        return any
            .subscribeOn(Schedulers.io())
            .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    val res = result.body()!!
//                    Log.d("성공", res.toString())
                    RxBus.instance.sendEvent(res, rxBusKey)
                },
                { error ->
                    if (retryCount > 0) {
                        buildDisposable(any, rxBusKey, retryCount - 1)
                    } else {
//                        Log.e("실패", error.toString())
                        RxBus.instance.sendEvent(error, RxBus.ERROR)
                    }
                }
            )

    }
}