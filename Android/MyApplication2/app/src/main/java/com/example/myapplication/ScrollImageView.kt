package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View


class ScrollImageView : View {

    private var offset = 0
    private var differ = 0
    private var sourceRect = Rect()
    private var destRect = Rect()
    private var bitmap: Bitmap? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bitmap ?: return
        differ = height + offset - bitmap!!.height
        if (differ > 0) {
            sourceRect.set(0, offset, width, bitmap!!.height)
            destRect.set(0, 0, width, height - differ)
            canvas?.drawBitmap(bitmap!!, sourceRect, destRect, null)
            sourceRect.set(0, 0, width, differ)
            destRect.set(0, height - differ, width, height)
            canvas?.drawBitmap(bitmap!!, sourceRect, destRect, null)
        } else {
            sourceRect.set(0, offset, width, height + offset)
            destRect.set(0, 0, width, height)
            canvas?.drawBitmap(bitmap!!, sourceRect, destRect, null)
        }
        scrollBitmap()
    }

    private fun scrollBitmap() {
        Log.e("wjc", "scroll")
        postDelayed({
            if (offset >= bitmap!!.height) {
                offset -= bitmap!!.height
            } else {
                offset += 2
            }
            invalidate()
        }, 5)
    }

    fun setParams(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }
}