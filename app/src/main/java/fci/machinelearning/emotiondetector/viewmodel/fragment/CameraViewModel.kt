package fci.machinelearning.emotiondetector.viewmodel.fragment

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.otaliastudios.cameraview.Frame as CameraFrame
import fci.machinelearning.emotiondetector.business.faceDetector.FaceDetector
import fci.machinelearning.emotiondetector.business.model.CameraState
import fci.machinelearning.emotiondetector.business.model.Frame
import fci.machinelearning.emotiondetector.business.model.Size
import fci.machinelearning.emotiondetector.util.error
import fci.machinelearning.emotiondetector.util.info
import fci.machinelearning.emotiondetector.util.performAsync
import fci.machinelearning.emotiondetector.viewmodel.BaseSharedRxViewModel
import fci.machinelearning.emotiondetector.viewmodel.Error
import fci.machinelearning.emotiondetector.viewmodel.Event

class CameraViewModel : BaseSharedRxViewModel() {

    companion object {

        @JvmStatic
        fun getInstance(fragmentActivity: FragmentActivity) =
            ViewModelProviders
                .of(fragmentActivity)
                .get(CameraViewModel::class.java)

        lateinit var cameraState: CameraState
    }


    lateinit var faceDetector: FaceDetector

    lateinit var emotionDetector: FaceDetector

    var isDetectingFaces = false

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
                onSuccess = { facesBounds ->
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
                onSuccess = { facesBounds ->
                    info("detectEmotions")
                    info("${facesBounds.size} faces detected.")
                    performAsync(
                        action = {
                            Thread.sleep(3000)
                        },
                        onSuccess = {
                            faceDetector.clear()
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
