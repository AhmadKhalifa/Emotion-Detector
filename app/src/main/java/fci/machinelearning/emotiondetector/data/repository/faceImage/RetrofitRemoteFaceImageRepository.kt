package fci.machinelearning.emotiondetector.data.repository.faceImage

import com.google.gson.Gson
import fci.machinelearning.emotiondetector.data.payload.EmotionDetectionResponse
import fci.machinelearning.emotiondetector.util.BaseConnectionChecker
import fci.machinelearning.emotiondetector.util.debug
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

class RetrofitRemoteFaceImageRepository(connectionChecker: BaseConnectionChecker = BaseConnectionChecker.getDefault()) :
    BaseRemoteFaceImageRepository(connectionChecker) {

    interface FaceImageService {

        @Multipart
        @POST("/images")
        fun uploadFacesImages(
            @Part images: Array<MultipartBody.Part>
        ): Call<EmotionDetectionResponse>
    }

    override fun detectEmotions(idImagePair: List<Pair<String, ByteArray>>): EmotionDetectionResponse {
        debug("POST request to $BASE_URL/images with ${idImagePair.size} images.")
        val response = execute(create(FaceImageService::class.java).uploadFacesImages(prepareImages(idImagePair)))
        debug(Gson().toJson(response).toString())
        return response
    }

    private fun prepareImages(idImagePair: List<Pair<String, ByteArray>>) = idImagePair.map { pair ->
        MultipartBody.Part.createFormData(
            "images",
            pair.first,
            RequestBody.create(MediaType.parse("image/*"), pair.second)
        )
    }.toTypedArray()
}