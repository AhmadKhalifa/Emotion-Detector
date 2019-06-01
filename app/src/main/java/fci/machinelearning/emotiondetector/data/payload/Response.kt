package fci.machinelearning.emotiondetector.data.payload


class EmotionDetectionResponse {
    lateinit var results: List<EmotionDetectionResult>
}

class EmotionDetectionResult {
    lateinit var faceId: String
    var emotion: Int = -1
}