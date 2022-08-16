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
        errorString: String = RxBus.ERROR,
        retryCount: Int = 1
    ): Disposable {
        //TODO: - 시작, 종료 시 이벤트 버스로 던지기 - 프로그래스바 동작을 위함
        // or 최상위 뷰를 인자로 항상 받아서 이 함수에서 프로그래스바 처리
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
                        buildDisposable(any, rxBusKey, errorString,retryCount - 1)
                    } else {
//                        Log.e("실패", error.toString())
                        RxBus.instance.sendEvent(error, errorString)
                    }
                }
            )

    }
}