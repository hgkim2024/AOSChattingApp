package com.asusoft.chatapp.util.extension

import android.app.Activity
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.asusoft.chatapp.application.ChattingApplication
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

fun View.onClick(
    click: () -> Unit
) {
    clicks()
        .throttleFirst(ChattingApplication.THROTTLE, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe {
            click()
        }
}

// TODO: - 추후에 캐시 관련해서 효과적으로 코드 짤 것
fun ImageView.imageLoad(with: Activity, url: String?, @DrawableRes resourceId: Int, skipMemoryCache: Boolean = true) {
    Glide.with(with)
        .load(url)
        .placeholder(resourceId)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(skipMemoryCache)
        .into(this)
}

fun ImageView.imageLoad(with: View, url: String?, @DrawableRes resourceId: Int, skipMemoryCache: Boolean = true) {
    Glide.with(with)
        .load(url)
        .placeholder(resourceId)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(skipMemoryCache)
        .into(this)
}