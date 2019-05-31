package fci.machinelearning.emotiondetector.business.faceDetector

import android.widget.Toast
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import android.graphics.*
import android.graphics.Bitmap
import android.os.AsyncTask
import java.io.ByteArrayOutputStream
import android.util.Log
import fci.machinelearning.emotiondetector.business.faceBounds.FaceBoundsOverlay
import fci.machinelearning.emotiondetector.business.faceBounds.FaceBoundsOverlayHandler
import fci.machinelearning.emotiondetector.business.model.Emotion
import fci.machinelearning.emotiondetector.business.model.FaceBounds
import fci.machinelearning.emotiondetector.business.model.Frame
import java.util.*
import kotlin.collections.ArrayList


class FaceDetector(private val faceBoundsOverlay: FaceBoundsOverlay) {

    private val faceBoundsOverlayHandler = FaceBoundsOverlayHandler()
    private val firebaseFaceDetectorWrapper = FirebaseFaceDetectorWrapper()

    fun process(frame: Frame) {
        updateOverlayAttributes(frame)
        detectFacesIn(frame)
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

    fun cropBitmap(bitmap: Bitmap, rect: Rect): Bitmap {
        val width = rect.right - rect.left
        val height = rect.bottom - rect.top
        val ret = Bitmap.createBitmap(width, height, bitmap.config)
        val canvas = Canvas(ret)
        canvas.drawBitmap(bitmap, -rect.left * 1.0f, -rect.top * 1.0f, null)
        return ret
    }

    fun prepareBitMap(fullIBitmap: Bitmap, croppingRect: Rect): ByteArray {
        val bitmap = cropBitmap(fullIBitmap, croppingRect)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val byteArray = stream.toByteArray()
        bitmap.recycle()
        return byteArray
    }

    private lateinit var asyncTask: AsyncTask<Void, Void, List<FaceBounds>>


    private fun detectFacesIn(frame: Frame) {
        frame.data?.let {
            val image = convertFrameToImage(frame)
            firebaseFaceDetectorWrapper.process(
                    image = image,
                    onSuccess = {
//                        asyncTask = object : AsyncTask<Void, Void, List<FaceBounds>>() {
//                            override fun doInBackground(vararg p0: Void?): List<FaceBounds> {
//                                isTaskLocked = true
//                                val faceBounds = convertToListOfFaceBounds(it)
//                                if (faceBounds.isNotEmpty()) {
//                                    val bitmapImg = image.bitmapForDebugging
//                                    val images = arrayListOf<Pair<String, ByteArray>>()
//                                    faceBounds.forEach { bounds ->
//                                        images.add(Pair("${bounds.id}", prepareBitMap(bitmapImg, bounds.box)))
//                                    }
//                                    val response = Client.uploadImagesToServer(images)
//                                    if (response.isSuccessful) {
//                                        val body = response.body()
//                                        if (body is Client.EmotionDetectionResponse) {
//                                            val faceBoundsMap = HashMap<String, FaceBounds>().apply {
//                                                faceBounds.forEach { bound ->
//                                                    put("${bound.id}", bound)
//                                                }
//                                            }
//                                            body.results.forEach { result ->
//                                                if (faceBoundsMap.containsKey(result.faceId)) {
//                                                    faceBoundsMap[result.faceId]?.emotion =
//                                                            Emotion.values()[result.emotion]
//                                                }
//                                            }
//                                        }
//                                    } else {
//                                        Log.e("FaceDetector", "${response.code()}")
//                                    }
//                                }
//                                return faceBounds
//                            }
//
//                            override fun onPostExecute(result: List<FaceBounds>?) {
//                                faceBoundsOverlay.updateFaces(result ?: ArrayList())
//                                MainActivity.isProcessing = false
//                                isTaskLocked = false
//                            }
//                        }
//                        if (it.isNotEmpty() && !isTaskLocked) {
//                            asyncTask.execute()
//                        } else {
//                            faceBoundsOverlay.updateFaces(ArrayList())
//                            MainActivity.isProcessing = false
//                        }
                    },
                    onError = {
                        Toast.makeText(faceBoundsOverlay.context, "Error processing images: $it", Toast.LENGTH_LONG).show()
                    })
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

    companion object {
        private const val RIGHT_ANGLE = 90

        private var isTaskLocked = false
    }
}