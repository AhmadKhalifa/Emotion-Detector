package fci.machinelearning.emotiondetector.util

import fci.machinelearning.emotiondetector.EmotionDetectorApplication

/**
 * @author Ahmad Khalifa
 */

abstract class BaseConnectionChecker {

    companion object {

        fun getDefault() = InternetConnectionChecker(EmotionDetectorApplication.instance)
    }

    abstract fun isNetworkAvailable(): Boolean
}
