package fci.machinelearning.emotiondetector.business.model

import android.graphics.Rect
import androidx.annotation.DrawableRes
import fci.machinelearning.emotiondetector.R

enum class Emotion(@DrawableRes val resId: Int) {
    SAD(R.drawable.sad),
    FEAR(R.drawable.fear),
    SURPRISE(R.drawable.surprise),
    DISGUST(R.drawable.disgust),
    HAPPY(R.drawable.happy),
    NEUTRAL(R.drawable.neutral),
    ANGRY(R.drawable.angry)
}

data class FaceBounds(val id: Int, val box: Rect, var emotion: Emotion = Emotion.NEUTRAL)

data class Frame(
    val data: ByteArray?,
    val rotation: Int,
    val size: Size,
    val format: Int,
    val isCameraFacingBack: Boolean)

data class Size(val width: Int, val height: Int)