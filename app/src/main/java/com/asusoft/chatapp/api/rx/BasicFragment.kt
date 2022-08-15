package com.asusoft.chatapp.api.rx

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BasicFragment: Fragment() {

    protected val disposables by lazy {
        CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}