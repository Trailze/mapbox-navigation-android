package com.mapbox.navigation.ui.maps.camera.view

import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.mapbox.navigation.ui.maps.R
import com.mapbox.navigation.ui.maps.databinding.MapboxRecenterLayoutBinding
import com.mapbox.navigation.ui.utils.internal.extensions.afterMeasured
import com.mapbox.navigation.ui.utils.internal.extensions.extend
import com.mapbox.navigation.ui.utils.internal.extensions.shrink
import com.mapbox.navigation.ui.utils.internal.extensions.slideWidth

/**
 * Default view to allow user to switch to route overview mode.
 */
class MapboxRecenterButton : ConstraintLayout {

    private var textWidth = 0
    private var isAnimationRunning = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private val binding = MapboxRecenterLayoutBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    /**
     *
     * @param context Context
     * @constructor
     */
    constructor(context: Context) : super(context)

    /**
     *
     * @param context Context
     * @param attrs AttributeSet?
     * @constructor
     */
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(attrs)
    }

    /**
     *
     * @param context Context
     * @param attrs AttributeSet?
     * @param defStyleAttr Int
     * @constructor
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initAttributes(attrs)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.recenterText.afterMeasured {
            textWidth = width
        }
    }

    /**
     * Allows you to change the style of [MapboxRecenterButton].
     * @param style Int
     */
    fun updateStyle(@StyleRes style: Int) {
        val typedArray = context.obtainStyledAttributes(
            style,
            R.styleable.MapboxRecenterButton
        )
        applyAttributes(typedArray)
        typedArray.recycle()
    }

    /**
     * Invoke the function to show optional text associated with the view.
     * @param duration for the view to be in the extended mode before it starts to shrink.
     */
    fun showTextAndExtend(duration: Long) {
        if (!isAnimationRunning) {
            isAnimationRunning = true
            val extendToWidth = EXTEND_TO_WIDTH * context.resources.displayMetrics.density
            val animator = getAnimator(textWidth, extendToWidth.toInt())
            binding.recenterText.extend(
                animator,
                doOnStart = {
                    binding.recenterText.text = context.getString(R.string.mapbox_recenter)
                    binding.recenterText.visibility = View.VISIBLE
                    mainHandler.postDelayed(
                        {
                            val endAnimator = getAnimator(extendToWidth.toInt(), textWidth)
                            binding.recenterText.shrink(
                                endAnimator,
                                doOnStart = {
                                    binding.recenterText.text = null
                                },
                                doOnEnd = {
                                    binding.recenterText.visibility = View.INVISIBLE
                                    isAnimationRunning = false
                                }
                            )
                        },
                        duration
                    )
                }
            )
        }
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.MapboxRecenterButton,
            0,
            R.style.MapboxStyleRecenter
        )
        applyAttributes(typedArray)
        typedArray.recycle()
    }

    private fun applyAttributes(typedArray: TypedArray) {
        typedArray.getDrawable(
            R.styleable.MapboxRecenterButton_recenterButtonDrawable
        ).also { binding.recenterIcon.setImageDrawable(it) }
    }

    private fun getAnimator(from: Int, to: Int) =
        binding.recenterText.slideWidth(from, to, SLIDE_DURATION)

    private companion object {
        private const val SLIDE_DURATION = 300L
        private const val EXTEND_TO_WIDTH = 150
    }
}
