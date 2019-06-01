package fci.machinelearning.emotiondetector.data.repository.faceImage

import fci.machinelearning.emotiondetector.data.payload.EmotionDetectionResponse
import fci.machinelearning.emotiondetector.data.repository.BaseRemoteRepository
import fci.machinelearning.emotiondetector.util.BaseConnectionChecker
import fci.machinelearning.emotiondetector.util.FACE_IMAGES_REPOSITORY_BASE_URL

abstract class BaseRemoteFaceImageRepository (connectionChecker: BaseConnectionChecker) :
        BaseRemoteRepository(connectionChecker, BASE_URL) {

    companion object {

        @JvmStatic
        protected val BASE_URL = FACE_IMAGES_REPOSITORY_BASE_URL

        protected const val CONNECT_TIME_OUT_SECONDS = 60

        protected const val READ_TIME_OUT_SECONDS = 60

        fun getDefault() = RetrofitRemoteFaceImageRepository()
    }

    abstract fun detectEmotions(idImagePair: List<Pair<String, ByteArray>>): EmotionDetectionResponse
}