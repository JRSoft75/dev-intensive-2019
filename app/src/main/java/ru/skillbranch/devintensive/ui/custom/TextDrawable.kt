package ru.skillbranch.devintensive.ui.custom

import android.graphics.*
import android.graphics.drawable.Drawable


class TextDrawable(
    private val text: String,
    private val textColor: Int = Color.WHITE,
    private val bgColor: Int = Color.BLACK
) : Drawable() {
    private val paint: Paint = Paint()

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        paint.color = bgColor
        canvas.drawRect(bounds, paint)
        paint.color = textColor
        canvas.drawText(
//            text, 0, text.length, bounds.centerX().toFloat(), bounds.centerY().toFloat(), paint   //for mix lower/upper case
            text, 0, text.length, bounds.centerX().toFloat(), bounds.centerY().toFloat() - paint.ascent() / 2, paint  //for upper case
        )

    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    init {
        paint.textSize = 30f
        paint.isAntiAlias = true
        paint.isFakeBoldText = true
        paint.setShadowLayer(6f, 0F, 0F, Color.BLACK)
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.CENTER
    }
}