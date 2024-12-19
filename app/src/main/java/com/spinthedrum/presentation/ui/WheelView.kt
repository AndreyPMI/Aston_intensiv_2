package com.spinthedrum.presentation.ui

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import kotlin.math.min
import kotlin.random.Random

class WheelView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    WheelViewInterface {
    private val colors = intArrayOf(
        Color.RED,
        Color.rgb(255, 165, 0),
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.BLUE,
        Color.rgb(128, 0, 128)
    )
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()
    private var rotationAngle = 0f
    private val animationDuration = 2000L
    private val segmentCount = 7
    private var animation: ValueAnimator? = null


    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawWheel(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = min(resolveSize(suggestedMinimumWidth, widthMeasureSpec), resolveSize(suggestedMinimumHeight, heightMeasureSpec))
        setMeasuredDimension(size, size)
    }


    private fun drawWheel(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (min(width, height) / 2f) * 0.8f
        val startAngle = rotationAngle + 295f
        val sweepAngle = 360f / segmentCount
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        for (i in 0 until segmentCount) {
            val angle = startAngle + (sweepAngle * i)
            paint.color = colors[6-i]
            canvas.drawArc(rectF, angle, sweepAngle, true, paint)
        }
    }

    override fun rotate(animatorListener: AnimatorListenerAdapter) {
        val startRotation = rotationAngle
        val randomRotation = Random.nextInt(1, 8)
        val endRotation = startRotation + (360f / 7f * randomRotation + 360f)
        animation?.cancel()
        animation = ValueAnimator.ofFloat(startRotation, endRotation)
        animation?.duration = animationDuration
        animation?.interpolator = DecelerateInterpolator()
        animation?.addUpdateListener {
            rotationAngle = it.animatedValue as Float
            invalidate()
        }
        animation?.addListener(animatorListener)
        animation?.start()
    }
    override fun getSelectedSectorIndex(): Int {
        val shiftedRotation = rotationAngle ;
        val normalizedRotation = shiftedRotation % 360
        val sectorAngle = 51f
        val selectedSector = (normalizedRotation / sectorAngle).toInt()
        return  selectedSector
    }
    override fun getSelectedColor(): Int {
        val selectedSectorIndex = getSelectedSectorIndex()
        val shiftedIndex = (selectedSectorIndex) % colors.size;
        val selectedColor = colors[shiftedIndex]
        return selectedColor
    }
    override fun setWheelSize(size: Int){
        minimumHeight = size + 400
        minimumWidth = size + 400
        val params = layoutParams
        params.height = size + 400
        params.width = size + 400
        layoutParams = params
        requestLayout()
        invalidate()
    }
}

