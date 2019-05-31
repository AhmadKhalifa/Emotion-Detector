package fci.machinelearning.emotiondetector

import android.app.Application
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

class EmotionDetectorApplication : Application() {

    companion object {

        lateinit var instance: EmotionDetectorApplication

        fun getString(@StringRes resId: Int): String = instance.getString(resId)

        fun getDrawable(@DrawableRes resId: Int) = ContextCompat.getDrawable(instance, resId)

        fun getColor(@ColorRes resId: Int) = ContextCompat.getColor(instance, resId)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}