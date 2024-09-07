package com.kathayat.railscarmaapplication.customviews

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.kathayat.railscarmaapplication.R


class GridOverlayView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private val gridPaint = Paint()
    private val pathPaint = Paint()
    private val path = Path()
    private val gridRows = 20
    private val gridCols = 20
    private val gridCellSize = 100f

    private val markerIcon: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_wifi_black)
    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 40f
        typeface = Typeface.DEFAULT_BOLD
        textAlign = Paint.Align.CENTER
    }

    private var startPoint = PointF(500f, 800f)
    private var destinationPoint: PointF? = null
    private val grid = Array(gridRows) { BooleanArray(gridCols) { true } }

    init {
        gridPaint.color = Color.LTGRAY
        gridPaint.strokeWidth = 3f
        gridPaint.isAntiAlias = true

        pathPaint.color = Color.RED
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth = 10f
        pathPaint.isAntiAlias = true
        isClickable = true

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val clickedX = event.x
            val clickedY = event.y

            val clickedCol = (clickedX / gridCellSize).toInt()
            val clickedRow = (clickedY / gridCellSize).toInt()
            if (clickedRow in 0 until gridRows && clickedCol in 0 until gridCols) {
                if (isValidCell(clickedRow, clickedCol)) {
                    destinationPoint = PointF(clickedX, clickedY)
                    path.reset()
                    drawRoute(clickedRow, clickedCol)
                    invalidate()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun isValidCell(row: Int, col: Int): Boolean {
        if (row in 0 until gridRows && col in 0 until gridCols) {
            return grid[row][col]
        }
        return false
    }

    private fun drawRoute(destRow: Int, destCol: Int) {
        path.moveTo(startPoint.x, startPoint.y)

        val startCol = (startPoint.x / gridCellSize).toInt()
        val startRow = (startPoint.y / gridCellSize).toInt()

        var currentRow = startRow
        var currentCol = startCol

        while (currentRow != destRow || currentCol != destCol) {
            if (currentCol < destCol && isValidCell(currentRow, currentCol + 1)) {
                currentCol++
            } else if (currentCol > destCol && isValidCell(currentRow, currentCol - 1)) {
                currentCol--
            } else if (currentRow < destRow && isValidCell(currentRow + 1, currentCol)) {
                currentRow++
            } else if (currentRow > destRow && isValidCell(currentRow - 1, currentCol)) {
                currentRow--
            }

            path.lineTo(currentCol * gridCellSize + gridCellSize / 2, currentRow * gridCellSize + gridCellSize / 2)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawGrid(canvas)

        canvas.drawPath(path, pathPaint)
        destinationPoint?.let { point ->
            drawMarker(canvas, point)
        }
    }

    private fun drawGrid(canvas: Canvas) {
        for (i in 0..gridRows) {
            canvas.drawLine(0f, i * gridCellSize, width.toFloat(), i * gridCellSize, gridPaint)
        }
        for (i in 0..gridCols) {
            canvas.drawLine(i * gridCellSize, 0f, i * gridCellSize, height.toFloat(), gridPaint)
        }

        for (row in 0 until gridRows) {
            for (col in 0 until gridCols) {
                if (!grid[row][col]) {
                    val left = col * gridCellSize
                    val top = row * gridCellSize
                    val right = left + gridCellSize
                    val bottom = top + gridCellSize

                    val wallPaint = Paint().apply {
                        color = Color.GRAY
                        style = Paint.Style.FILL
                    }
                    canvas.drawRect(left, top, right, bottom, wallPaint)
                }
            }
        }
    }

    private fun drawMarker(canvas: Canvas?, point: PointF) {
        canvas?.drawBitmap(markerIcon, point.x - markerIcon.width / 2, point.y - markerIcon.height, null)
        canvas?.drawText("Point of Interest", point.x, point.y + 50f, textPaint)
    }
}







