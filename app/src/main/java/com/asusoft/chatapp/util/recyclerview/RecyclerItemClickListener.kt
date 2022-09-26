package com.asusoft.chatapp.util.recyclerview

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.asusoft.chatapp.application.ChattingApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class RecyclerItemClickListener(
    context: Context?,
    recyclerView: RecyclerView,
    private val mListener: OnItemClickListener?
) : RecyclerView.OnItemTouchListener {
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }

    private var preventDoubleClickFlag = true

    private val mGestureDetector: GestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            val childView = recyclerView.findChildViewUnder(e.x, e.y)
            if (childView != null && mListener != null) {
                mListener.onItemLongClick(
                    childView,
                    recyclerView.getChildAdapterPosition(childView)
                )
            }
        }
    })

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            if (preventDoubleClickFlag) {
                preventDoubleClickFlag = false

                GlobalScope.async(Dispatchers.Main) {
                    delay(RecyclerViewAdapter.CLICK_DELAY)
                    mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
                }

                GlobalScope.async {
                    delay(ChattingApplication.THROTTLE)
                    preventDoubleClickFlag = true
                }
            }
        }

        return false
    }

    override fun onTouchEvent(view: RecyclerView, motionEvent: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

}