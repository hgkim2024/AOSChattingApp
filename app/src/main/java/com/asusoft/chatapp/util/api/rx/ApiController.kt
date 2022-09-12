package com.asusoft.chatapp.util.api.rx

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.asusoft.chatapp.R
import com.asusoft.chatapp.util.extension.removeFromSuperView
import com.google.android.material.progressindicator.CircularProgressIndicator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

object ApiController {

    fun <T> apiSubscribe(
        any: Observable<T>,
        context: Context?,
        success: (Any?) -> Unit,
        fail: (e: Throwable) -> Unit
    ): Disposable {
        val indicator: List<View>?
        indicator = createActivityIndicator(context)

        return any
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    removeViewList(indicator)
                    success(result)
                },
                { error ->
                    removeViewList(indicator)
                    fail(error)
                }
            )

    }

    fun <T> apiSubscribe(
        any: Observable<T>,
        fragment: Fragment,
        success: (Any?) -> Unit,
        fail: (e: Throwable) -> Unit
    ): Disposable {
        val indicator: List<View>?
        indicator = createFragmentIndicator(fragment)

        return any
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    removeViewList(indicator)
                    success(result)
                },
                { error ->
                    removeViewList(indicator)
                    fail(error)
                }
            )

    }

    fun toast(ctx: Context?, msg: String?) {
        if (ctx == null) return
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
    }

    private fun createActivityIndicator(context: Context?): List<View> {
        val ctx = context ?: return ArrayList()
        val activity = (ctx as? Activity) ?: return ArrayList()
        val rootView = activity.window.decorView.rootView
        return createViewIndicator(ctx, rootView)
    }

    private fun createFragmentIndicator(fragment: Fragment): List<View> {
        return createViewIndicator(fragment.context, fragment.view)
    }

    private fun createViewIndicator(
        context: Context?,
        rootView: View?
    ): List<View> {
        val ctx = context ?: return ArrayList()
        val root = rootView ?: return ArrayList()

        val rootLayout = root.findViewById<ConstraintLayout>(R.id.root)

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