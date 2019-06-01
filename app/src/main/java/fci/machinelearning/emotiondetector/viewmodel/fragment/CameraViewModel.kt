package fci.machinelearning.emotiondetector.viewmodel.fragment

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.otaliastudios.cameraview.Frame as CameraFrame
import fci.machinelearning.emotiondetector.business.faceDetector.FaceDetector
import fci.machinelearning.emotiondetector.business.model.*
import fci.machinelearning.emotiondetector.data.repository.faceImage.BaseRemoteFaceImageRepository
import fci.machinelearning.emotiondetector.util.*
import fci.machinelearning.emotiondetector.viewmodel.BaseSharedRxViewModel
import fci.machinelearning.emotiondetector.viewmodel.Error
import fci.machinelearning.emotiondetector.viewmodel.Event
import java.util.HashMap

class CameraViewModel : BaseSharedRxViewModel() {

    companion object {

        @JvmStatic
        fun getInstance(fragmentActivity: FragmentActivity) =
            ViewModelProviders
                .of(fragmentActivity)
                .get(CameraViewModel::class.java)

        lateinit var cameraState: CameraState
    }

    private val remoteFaceImageRepository by lazy { BaseRemoteFaceImageRepository.getDefault() }

    lateinit var faceDetector: FaceDetector

    lateinit var emotionDetector: FaceDetector

    private var isDetectingFaces = false

    private fun getFrame(cameraFrame: CameraFrame) = Frame(
        cameraFrame.data,
        cameraFrame.rotation,
        Size(cameraFrame.size.width, cameraFrame.size.height),
        cameraFrame.format,
        isCameraFacingBack = true
    )

    fun detectFaces(cameraFrame: CameraFrame) {
        if (cameraState == CameraState.NORMAL && !isDetectingFaces) {
            isDetectingFaces = true
            faceDetector.detectFaces(
                frame = getFrame(cameraFrame),
                onSuccess = { _, facesBounds ->
                    info("detectFaces")
                    info("${facesBounds.size} faces detected.")
                    faceDetector.updateFaceFrames(facesBounds)
                    isDetectingFaces = false
                },
                onError = {
                    notify(Error.FIREBASE_ERROR)
                    error("Error detecting faces", it)
                    isDetectingFaces = false
                }
            )
        }
    }


    fun detectEmotions(cameraFrame: CameraFrame) {
        if (cameraState == CameraState.PRE_PROCESSING) {
            notify(Event.EMOTION_PROCESSING_STARTED)
            emotionDetector.detectFaces(
                frame = getFrame(cameraFrame),
                onSuccess = { image, facesBounds ->
                    info("detectEmotions")
                    info("${facesBounds.size} faces detected.")
                    faceDetector.clear()
                    performAsync(
                        action = {
                            if (facesBounds.isNotEmpty()) {
                                val emotions = remoteFaceImageRepository.detectEmotions(facesBounds.map { bounds ->
                                    Pair("${bounds.id}", BitmapUtils.run { toByteArray(crop(image, bounds.box)) })
                                }).results
                                val faceBoundsMap = HashMap<String, FaceBounds>().apply {
                                    facesBounds.forEach { bound -> put("${bound.id}", bound) }
                                }
                                emotions.forEach { emotion ->
                                    if (faceBoundsMap.containsKey(emotion.faceId)) {
                                        faceBoundsMap[emotion.faceId]?.emotion =
                                            Emotion.values()[emotion.emotion]
                                    }
                                }
                            }
                        },
                        onSuccess = {
                            emotionDetector.updateFaceFrames(facesBounds)
                            notify(Event.EMOTION_PROCESSING_FINISHED)
                        },
                        onFailure = { throwable ->
                            notify(
                                if (throwable is NoInternetConnectionException) Error.INTERNET_CONNECTION_ERROR
                                else Error.GENERAL_ERROR
                            )
                            emotionDetector.updateFaceFrames(facesBounds)
                            notify(Event.EMOTION_PROCESSING_FINISHED)
                        }
                    )
                },
                onError = {
                    notify(Event.EMOTION_PROCESSING_FINISHED)
                    notify(Error.FIREBASE_ERROR)
                    error("Error detecting faces", it)
                }
            )
        }
    }

    fun clearFaceBounds() = faceDetector.clear()

    fun clearEmotions() = emotionDetector.clear()
}
