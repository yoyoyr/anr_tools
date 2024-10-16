package com.asm.privacymethodhooker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

/**
 * 位置指示器。
 */
class TestView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var mPaint: Paint = Paint().apply {
        color = Color.RED
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4.dpf() //4
    }

    var paint: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 1.dpf() //4
    }


    //*5
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        val rectfLeft = RectF(0.dpf(), 40.dpf(), 20.dpf(), 50.dpf())
        canvas?.drawRect(rectfLeft,paint)
        canvas?.drawArc(rectfLeft, 0f, 90f, false, mPaint)
        val rectf = RectF(20.dpf(),0.dpf(),250.dpf(),250.dpf())
        canvas?.drawRect(rectf,paint)
//        canvas?.drawArc(rectf, 0f, 360f, false, mPaint)

//        val path = Path()
//        path.moveTo(0.dpf(), 100.dpf())
//        path.quadTo(10.dpf(), 100.dpf(), 20.dpf(), 90.dpf())
//        path.quadTo(240.dpf(), 10.dpf(), 240.dpf(), 0.dpf())
//        canvas?.drawPath(path, mPaint)
    }

}

fun Int.dpf(): Float {
    return DeviceUtils.dip2fpx(this)
}