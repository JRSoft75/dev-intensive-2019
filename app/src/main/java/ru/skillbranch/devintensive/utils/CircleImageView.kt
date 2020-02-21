package ru.skillbranch.devintensive.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.dpToPx
import ru.skillbranch.devintensive.extensions.pxToDp
import kotlin.math.min


//fun CircleImageView(context: Context?) {
//    super(context)
//    init()
//}
//
//fun CircleImageView(
//    context: Context?,
//    attrs: AttributeSet?
//) {
//    this(context, attrs, 0)
//}

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr:Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object{
        private val SCALE_TYPE = ScaleType.CENTER_CROP
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private val COLORDRAWABLE_DIMENSION = 2
        private val DEFAULT_BORDER_WIDTH = 2
        private val DEFAULT_BORDER_COLOR = Color.WHITE
        private val DEFAULT_CIRCLE_BACKGROUND_COLOR = Color.TRANSPARENT
        private val DEFAULT_BORDER_OVERLAY = false
    }

    private var mDrawableRect = RectF()
    private var mBorderRect = RectF()

    private var mShaderMatrix = Matrix()
    private var mBitmapPaint = Paint()
    private var mBorderPaint = Paint()
    private var mCircleBackgroundPaint = Paint()

    private var borderColor =
        DEFAULT_BORDER_COLOR
    @Px
    var borderWidth: Float = context.dpToPx(DEFAULT_BORDER_WIDTH)
    private var mCircleBackgroundColor =
        DEFAULT_CIRCLE_BACKGROUND_COLOR

    private var mBitmap: Bitmap? = null
    private var mBitmapShader: BitmapShader? = null
    private var mBitmapWidth = 0
    private var mBitmapHeight = 0

    private var mDrawableRadius = 0f
    private var mBorderRadius = 0f

    private var mColorFilter: ColorFilter? = null

    private var mReady = false
    private var mSetupPending = false
    private var mBorderOverlay = false
    private var mDisableCircularTransformation = false


    init{
        if(attrs!=null){
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            this.borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor,
                DEFAULT_BORDER_COLOR
            )
            this.borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, context.dpToPx(
                DEFAULT_BORDER_WIDTH
            ))

            super.setScaleType(SCALE_TYPE)
            mReady = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                outlineProvider = OutlineProvider()
            }
            if (mSetupPending) {
                setup()
                mSetupPending = false
            }
            a.recycle()
        }
    }



    override fun getScaleType(): ScaleType? {
        return SCALE_TYPE
    }

    override fun setScaleType(scaleType: ScaleType) {
        require(scaleType == SCALE_TYPE) {
            String.format(
                "ScaleType %s not supported.",
                scaleType
            )
        }
    }

    override fun setAdjustViewBounds(adjustViewBounds: Boolean) {
        require(!adjustViewBounds) { "adjustViewBounds not supported." }
    }

    override fun onDraw(canvas: Canvas) {
        if (mDisableCircularTransformation) {
            super.onDraw(canvas)
            return
        }
        if (mBitmap == null) {
            return
        }
        if (mCircleBackgroundColor !== Color.TRANSPARENT) {
            canvas.drawCircle(
                mDrawableRect.centerX(),
                mDrawableRect.centerY(),
                mDrawableRadius,
                mCircleBackgroundPaint
            )
        }
        canvas.drawCircle(
            mDrawableRect.centerX(),
            mDrawableRect.centerY(),
            mDrawableRadius,
            mBitmapPaint
        )
        if (borderWidth > 0) {
            canvas.drawCircle(
                mBorderRect.centerX(),
                mBorderRect.centerY(),
                mBorderRadius,
                mBorderPaint
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    fun getBorderColor(): Int {
        return this.borderColor
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        if (colorId == this.borderColor) {
            return
        }
        this.borderColor = colorId
        mBorderPaint.color = this.borderColor
        invalidate()
    }

    fun setBorderColor( hex: String) {
        if (Color.parseColor(hex) == this.borderColor) {
            return
        }
        this.borderColor = Color.parseColor(hex)
        mBorderPaint.color = this.borderColor
        invalidate()
    }

    fun getCircleBackgroundColor(): Int {
        return mCircleBackgroundColor
    }

    fun setCircleBackgroundColor(@ColorInt circleBackgroundColor: Int) {
        if (circleBackgroundColor == mCircleBackgroundColor) {
            return
        }
        mCircleBackgroundColor = circleBackgroundColor
        mCircleBackgroundPaint.color = circleBackgroundColor
        invalidate()
    }

    fun setCircleBackgroundColorResource(@ColorRes circleBackgroundRes: Int) {
        setCircleBackgroundColor(context.resources.getColor(circleBackgroundRes))
    }

    fun getBorderWidth(): Int {
        return context.pxToDp(borderWidth)
    }

    fun setBorderWidth(@Dimension dp: Int) {
        if (context.dpToPx(dp) == this.borderWidth) {
            return
        }
        this.borderWidth = context.dpToPx(dp)
        setup()
    }

    fun isBorderOverlay(): Boolean {
        return mBorderOverlay
    }

    fun setBorderOverlay(borderOverlay: Boolean) {
        if (borderOverlay == mBorderOverlay) {
            return
        }
        mBorderOverlay = borderOverlay
        setup()
    }

    fun isDisableCircularTransformation(): Boolean {
        return mDisableCircularTransformation
    }

    fun setDisableCircularTransformation(disableCircularTransformation: Boolean) {
        if (mDisableCircularTransformation === disableCircularTransformation) {
            return
        }
        mDisableCircularTransformation = disableCircularTransformation
        initializeBitmap()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        initializeBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initializeBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initializeBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initializeBitmap()
    }

    override fun setColorFilter(cf: ColorFilter) {
        if (cf === mColorFilter) {
            return
        }
        mColorFilter = cf
        applyColorFilter()
        invalidate()
    }

    override fun getColorFilter(): ColorFilter? {
        return mColorFilter
    }

    private fun applyColorFilter() { // This might be called from setColorFilter during ImageView construction
// before member initialization has finished on API level <= 19.
        if (mBitmapPaint != null) {
            mBitmapPaint.colorFilter = mColorFilter
        }
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        }else if (drawable is TextDrawable) {
            drawableToBitmap(drawable)
        } else try {
            val bitmap: Bitmap
            bitmap = if (drawable is ColorDrawable) {
                Bitmap.createBitmap(
                    COLORDRAWABLE_DIMENSION,
                    COLORDRAWABLE_DIMENSION,
                    BITMAP_CONFIG
                )
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    BITMAP_CONFIG
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun initializeBitmap() {
        if (mDisableCircularTransformation) {
            mBitmap = null
        } else {
            mBitmap = getBitmapFromDrawable(drawable)
        }
        setup()
    }

    private fun setup() {
        if (!mReady) {
            mSetupPending = true
            return
        }
        if (width == 0 && height == 0) {
            return
        }
        if (mBitmap == null) {
            invalidate()
            return
        }
        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.isAntiAlias = true
        mBitmapPaint.isDither = true
        mBitmapPaint.isFilterBitmap = true
        mBitmapPaint.shader = mBitmapShader
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = this.borderColor
        mBorderPaint.strokeWidth = borderWidth.toFloat()
        mCircleBackgroundPaint.style = Paint.Style.FILL
        mCircleBackgroundPaint.isAntiAlias = true
        mCircleBackgroundPaint.color = mCircleBackgroundColor
        mBitmapHeight = mBitmap!!.height
        mBitmapWidth = mBitmap!!.width
        mBorderRect.set(calculateBounds())
        mBorderRadius = Math.min(
            (mBorderRect.height() - borderWidth) / 2.0f,
            (mBorderRect.width() - borderWidth) / 2.0f
        )
        mDrawableRect.set(mBorderRect)
        if (!mBorderOverlay && borderWidth > 0) {
            mDrawableRect.inset(borderWidth - 1.0f, borderWidth - 1.0f)
        }
        mDrawableRadius =
            Math.min(mDrawableRect.height() / 2.0f, mDrawableRect.width() / 2.0f)
        applyColorFilter()
        updateShaderMatrix()
        invalidate()
    }

    private fun calculateBounds(): RectF? {
        val availableWidth = width - paddingLeft - paddingRight
        val availableHeight = height - paddingTop - paddingBottom
        val sideLength = min(availableWidth, availableHeight)
        val left = paddingLeft + (availableWidth - sideLength) / 2f
        val top = paddingTop + (availableHeight - sideLength) / 2f
        return RectF(left, top, left + sideLength, top + sideLength)
    }

    private fun updateShaderMatrix() {
        val scale: Float
        var dx = 0f
        var dy = 0f
        mShaderMatrix.set(null)
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / mBitmapHeight
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f
        } else {
            scale = mDrawableRect.width() / mBitmapWidth
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(
            (dx + 0.5f).toInt() + mDrawableRect.left,
            (dy + 0.5f).toInt() + mDrawableRect.top
        )
        mBitmapShader?.setLocalMatrix(mShaderMatrix)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (mDisableCircularTransformation) {
            super.onTouchEvent(event)
        } else inTouchableArea(event.x, event.y) && super.onTouchEvent(event)
    }

    private fun inTouchableArea(x: Float, y: Float): Boolean {
        return if (mBorderRect.isEmpty) {
            true
        } else Math.pow(
            (x - mBorderRect.centerX()).toDouble(),
            2.0
        ) + Math.pow((y - mBorderRect.centerY()).toDouble(), 2.0) <= Math.pow(
            mBorderRadius.toDouble(),
            2.0
        )
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 96 // Replaced the 1 by a 96
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 96 // Replaced the 1 by a 96
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    inner class OutlineProvider : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline) {
            if (mDisableCircularTransformation) {
                BACKGROUND.getOutline(view, outline)
            } else {
                val bounds = Rect()
                mBorderRect.roundOut(bounds)
                outline.setRoundRect(bounds, bounds.width() / 2.0f)
            }
        }
    }
}