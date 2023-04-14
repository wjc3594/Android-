package com.example.myapplication

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout

class RoundConstraintLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, def: Int = 0
) : ConstraintLayout(context, attrs, def) {

    val path:Path
    var rect:RectF
    val radius:FloatArray
    init {
        path=Path()
        rect=RectF()
        radius= floatArrayOf(80f,80f,80f,80f,0f,0f,0f,0f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect.set(0f,0f,w.toFloat(),h.toFloat())
        Log.i("wjc","onSizeChanged:w=$w,h=$h,oldW=$oldw,oldH=$oldh")
    }

    override fun draw(canvas: Canvas?) {
        path.addRoundRect(rect, radius, Path.Direction.CW)
        canvas?.clipPath(path)
        super.draw(canvas)
    }
}