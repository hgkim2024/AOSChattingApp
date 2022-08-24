package com.asusoft.chatapp.api.rx

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.asusoft.chatapp.R
import com.asusoft.chatapp.extension.removeFromSuperView
import com.google.android.material.progressindicator.CircularProgressIndicator
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

object ApiClient {


    fun <T> buildDisposable(
        any: Maybe<Response<T>>,
        rxBusKey: String,
        errorString: String = RxBus.ERROR,
        context: Context?,
        retryCount: Int = 1
    ): Disposable {
        val indicator: List<View>?
        indicator = createIndicator(context)

        return any
            .subscribeOn(Schedulers.io())
            .map { t -> if (t.isSuccessful) t else throw HttpException(t) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    removeViewList(indicator)
                    val res = result.body()!!
//                    Log.d("성공", res.toString())
                    RxBus.instance.sendEvent(res, rxBusKey)
                },
                { error ->
                    removeViewList(indicator)
                    if (retryCount > 0) {
                        buildDisposable(any, rxBusKey, errorString, context,retryCount - 1)
                    } else {
//                        Log.e("실패", error.toString())
                        if (error is SocketTimeoutException) {
                            RxBus.instance.sendEvent(error, RxBus.ERROR)
                        } else {
                            RxBus.instance.sendEvent(error, errorString)
                        }
                    }
                }
            )

    }


    private fun createIndicator(context: Context?): List<View> {
        val ctx = context ?: return ArrayList()
        val activity = (ctx as? Activity) ?: return ArrayList()
        val rootView = activity.window.decorView.rootView
        val rootLayout = rootView.findViewById<ConstraintLayout>(R.id.root)

        val backgroundView = ConstraintLayout(ctx)
        backgroundView.id = View.generateViewId()
        backgroundView.setBackgroundColor(Color.BLACK)
        backgroundView.alpha = 0.5F
        backgroundView.layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.MATCH_PARENT
        )


        val indicator = CircularProgressIndicator(ctx)
        indicator.isIndeterminate = true
        indicator.id = View.generateViewId()

        rootLayout.addView(backgroundView)
        rootLayout.addView(indicator)

        val set = ConstraintSet()
        set.clone(rootLayout)

        set.connect(backgroundView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(backgroundView.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.connect(backgroundView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        set.connect(backgroundView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        set.connect(indicator.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        set.connect(indicator.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
        set.connect(indicator.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        set.connect(indicator.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

        set.applyTo(rootLayout)
        backgroundView.isClickable = true
        indicator.show()

        return arrayListOf(indicator, backgroundView)
    }

    private fun removeViewList(viewList: List<View>?) {
        viewList?.forEach {
            it.removeFromSuperView()
        }
    }
}