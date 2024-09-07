package com.kathayat.railscarmaapplication.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.kathayat.railscarmaapplication.R
import java.util.Random
import kotlin.math.pow
import kotlin.math.sqrt

class WifiMarkerView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val markers = mutableListOf<WifiMarker>()

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
        isFakeBoldText = true
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    private val markerDrawable = ContextCompat.getDrawable(context, R.drawable.ic_wifi)
    private val gridSpacing = 100f

    fun addMarker(ssid: String, signalStrength: Int) {
        val (x, y) = generateNonOverlappingPosition()
        markers.add(WifiMarker(ssid, x, y, signalStrength))
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let {
            for (marker in markers) {
                val (ssid, x, y) = marker
                drawMarkerIcon(it, x, y)
                val textY = y + 80f
                it.drawText(ssid, x, textY, textPaint)
            }
        }
    }

    private fun generateNonOverlappingPosition(): Pair<Float, Float> {
        var x: Float
        var y: Float
        var attempt = 0
        val maxAttempts = 100

        do {
            x = (Math.random() * (width - gridSpacing)).toFloat()
            y = (Math.random() * (height - gridSpacing)).toFloat()
            attempt++
        } while (isOverlapping(x, y) && attempt < maxAttempts)

        try {
            x = x.coerceIn(gridSpacing, width - gridSpacing)
            y = y.coerceIn(gridSpacing, height - gridSpacing)
        } catch (e:Exception){
            Log.d("WifiMarkerLogs","Error generate NonOverlapping Position")
        }

        return Pair(x, y)
    }

    private fun isOverlapping(x: Float, y: Float): Boolean {
        val radius = gridSpacing / 2
        for (marker in markers) {
            val (existingX, existingY) = marker.x to marker.y
            val distance =
                sqrt(((x - existingX).toDouble().pow(2) + (y - existingY).toDouble().pow(2)))
            if (distance < radius) {
                return true
            }
        }
        return false
    }

    private fun drawMarkerIcon(canvas: Canvas, x: Float, y: Float) {
        markerDrawable?.let { drawable ->
            val markerSize = 100 // Adjust size as needed
            val left = (x - markerSize / 2).toInt()
            val top = (y - markerSize / 2).toInt()
            val right = (x + markerSize / 2).toInt()
            val bottom = (y + markerSize / 2).toInt()
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
    }

    data class WifiMarker(val ssid: String, val x: Float, val y: Float, val signalStrength: Int)
}
