package com.spinthedrum.presentation.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.spinthedrum.presentation.viewModel.SelectedColorText

class ResultView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var bitmap: Bitmap? = null
    private var text: String? = null
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 32f
        textAlign = Paint.Align.CENTER
    }
    private val textBounds = Rect()

    fun setContent(bitmap: Bitmap?, text: SelectedColorText?){
        this.bitmap = bitmap
        this.text = text?.toString()?:""
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, (width - bitmap!!.width) / 2f, (height - bitmap!!.height) / 2f, null)
        }
        if (text != null){
            textPaint.getTextBounds(text, 0, text!!.length, textBounds)
            canvas.drawText(text!!, width / 2f, height / 2f + textBounds.height()/2 , textPaint)
        }
    }
}