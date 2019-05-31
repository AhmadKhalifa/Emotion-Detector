package fci.machinelearning.emotiondetector.business.faceDetector

import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import fci.machinelearning.emotiondetector.ui.widget.FaceBoundsOverlay
import fci.machinelearning.emotiondetector.business.faceBounds.FaceBoundsOverlayHandler
import fci.machinelearning.emotiondetector.business.model.FaceBounds
import fci.machinelearning.emotiondetector.business.model.Frame

open class FaceDetector(private val faceBoundsOverlay: FaceBoundsOverlay) {

    private val faceBoundsOverlayHandler = FaceBoundsOverlayHandler()
    private val firebaseFaceDetectorWrapper = FirebaseFaceDetectorWrapper()

    companion object {
        private const val RIGHT_ANGLE = 90
    }

    fun detectFaces(frame: Frame, onSuccess: (List<FaceBounds>) -> Unit, onError: (Throwable) -> Unit = {}) {
        updateOverlayAttributes(frame)
        detectFacesIn(frame, onSuccess, onError)
    }

    private fun updateOverlayAttributes(frame: Frame) {
        faceBoundsOverlayHandler.updateOverlayAttributes(
            overlayWidth = frame.size.width,
            overlayHeight = frame.size.height,
            rotation = frame.rotation,
            isCameraFacingBack = frame.isCameraFacingBack,
            callback = { newWidth, newHeight, newOrientation, newFacing ->
                faceBoundsOverlay.cameraPreviewWidth = newWidth
                faceBoundsOverlay.cameraPreviewHeight = newHeight
                faceBoundsOverlay.cameraOrientation = newOrientation
                faceBoundsOverlay.cameraFacing = newFacing
            })
    }

    protected open fun detectFacesIn(frame: Frame,
                                     onSuccess: (List<FaceBounds>) -> Unit = {},
                                     onError: (Throwable) -> Unit = {}) {
        frame.data?.let {
            firebaseFaceDetectorWrapper.process(
                convertFrameToImage(frame),
                onSuccess = { firebaseVisionFaces ->
                    onSuccess(convertToListOfFaceBounds(firebaseVisionFaces))
                },
                onError = onError
            )
        }
    }

    private fun convertFrameToImage(frame: Frame) =
        FirebaseVisionImage.fromByteArray(frame.data!!, extractFrameMetadata(frame))

    private fun extractFrameMetadata(frame: Frame): FirebaseVisionImageMetadata =
        FirebaseVisionImageMetadata.Builder()
            .setWidth(frame.size.width)
            .setHeight(frame.size.height)
            .setFormat(frame.format)
            .setRotation(frame.rotation / RIGHT_ANGLE)
            .build()

    private fun convertToListOfFaceBounds(faces: MutableList<FirebaseVisionFace>): List<FaceBounds> =
        faces.map { FaceBounds(it.trackingId, it.boundingBox) }

    fun clear() {
        faceBoundsOverlay.updateFaces(listOf())
    }

    fun updateFaceFrames(facesBounds: List<FaceBounds>) {
        faceBoundsOverlay.updateFaces(facesBounds)
    }
}