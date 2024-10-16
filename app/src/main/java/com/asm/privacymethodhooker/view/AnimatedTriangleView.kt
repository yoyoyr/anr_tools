package com.asm.privacymethodhooker.view


import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.animation.ValueAnimator

class AnimatedTriangleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFFBC73C.toInt() // 黑色
        strokeWidth = 5f // 线宽
        style = Paint.Style.STROKE // 描边
    }

    private val sideLengthPx = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, 15f, resources.displayMetrics
    )

    // Point A's initial vertical offset (top corner)
    private var pointAOffsetY: Float = 0f
    private var animator = ValueAnimator.ofFloat(0f, sideLengthPx / 4f, sideLengthPx / 2f)

    var isDown = true
        private set


    fun switch() {
        if (isDown) {
            switchUp()
            isDown = false
        } else {
            switchDown()
            isDown = true
        }
    }

    fun switchUp() {
        paint.color = 0xFFFFFFFF.toInt()
        animator = ValueAnimator.ofFloat(sideLengthPx, sideLengthPx / 2f, 0f)
        animator.duration = 2000 // 动画时长为 2000 毫秒
        animator.addUpdateListener { animation ->
            pointAOffsetY = (animation.animatedValue as Float) / 2
            invalidate() // 请求重绘以更新视图
        }
        animator.start()
    }

    fun switchDown() {
        paint.color = 0xFFFBC73C.toInt()
        animator = ValueAnimator.ofFloat(0f, sideLengthPx / 2f, sideLengthPx)
        animator.duration = 2000 // 动画时长为 2000 毫秒
        animator.addUpdateListener { animation ->
            pointAOffsetY = (animation.animatedValue as Float) / 2
            invalidate() // 请求重绘以更新视图
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f + sideLengthPx / 4f // Initial position for BC is in the middle

        // Calculate the positions of the points based on the current state of the animation.
        val pointAx = centerX
        val pointAy = centerY - sideLengthPx / 2f + pointAOffsetY

        val pointBx = centerX - sideLengthPx / 2f
        val pointBy = centerY - pointAOffsetY

        val pointCx = centerX + sideLengthPx / 2f
        val pointCy = pointBy

        // Draw line segments AB and AC (not drawing BC edge)
        canvas.drawLine(pointAx, pointAy, pointBx, pointBy, paint)
        canvas.drawLine(pointAx, pointAy, pointCx, pointCy, paint)
    }
}
