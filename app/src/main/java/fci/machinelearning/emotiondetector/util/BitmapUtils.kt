package fci.machinelearning.emotiondetector.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import java.io.ByteArrayOutputStream

object BitmapUtils {

    fun crop(bitmap: Bitmap, rect: Rect): Bitmap {
        val width = rect.right - rect.left
        val height = rect.bottom - rect.top
        val ret = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(ret)
        canvas.drawBitmap(bitmap, -rect.left * 1.0f, -rect.top * 1.0f, null)
        return ret
    }

    fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        bitmap.recycle()
        return byteArray
    }
}