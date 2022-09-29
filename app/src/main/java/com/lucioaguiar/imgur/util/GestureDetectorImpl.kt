package com.lucioaguiar.imgur.util

import android.view.ScaleGestureDetector

class GestureDetectorImpl(spanCountval: Int, val changeSpanCount: (Int) -> Unit) : ScaleGestureDetector.OnScaleGestureListener {
    var quantitySpan = spanCountval
    var initialValue: Float? = null

    override fun onScale(p0: ScaleGestureDetector): Boolean {
        return false
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {

        initialValue?.let { value ->
            val v: Float = detector.currentSpan - value

            if ((v / 50).toInt() != 0) {
                if (v > 0) {
                    if (quantitySpan > 1) {
                        quantitySpan--
                    }
                } else {
                    if (quantitySpan < 5) {
                        quantitySpan++
                    }
                }
                initialValue = null
                changeSpanCount(quantitySpan)
            }
        }?: run {
            initialValue = detector.currentSpan
        }

        return false
    }

    override fun onScaleEnd(p0: ScaleGestureDetector) {
        initialValue = null
    }
}