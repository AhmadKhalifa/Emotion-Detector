package fci.machinelearning.emotiondetector.ui.fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fci.machinelearning.emotiondetector.EmotionDetectorApplication
import fci.machinelearning.emotiondetector.R
import fci.machinelearning.emotiondetector.business.faceDetector.FaceDetector
import fci.machinelearning.emotiondetector.business.model.CameraState
import fci.machinelearning.emotiondetector.ui.base.BaseFragment
import fci.machinelearning.emotiondetector.util.FragmentNotAttachedException
import fci.machinelearning.emotiondetector.viewmodel.Event
import fci.machinelearning.emotiondetector.viewmodel.fragment.CameraViewModel
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : BaseFragment<CameraViewModel>() {

    companion object {

        val TAG: String = CameraFragment::class.java.simpleName

        fun newInstance() = CameraFragment()
    }

    interface OnFragmentInteractionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_camera, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CameraViewModel.cameraState = CameraState.NORMAL
        viewModel.faceDetector = FaceDetector(facesBoundsOverlay)
        viewModel.emotionDetector = FaceDetector(facesEmotionsOverlay.apply { isEmotionsOverlay = true })
        cameraView.addFrameProcessor(viewModel::detectFaces)
        cameraButton.setOnClickListener {
            when (CameraViewModel.cameraState) {
                CameraState.NORMAL -> {
                    viewModel.clearFaceBounds()
                    detectEmotions()
                }
                CameraState.READY -> resumeCamera()
                else -> {

                }
            }
        }
    }

    private fun showProgress(show: Boolean) = progressLayout.apply {
        visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun getViewModelInstance() = activity?.run activity@{
        CameraViewModel.getInstance(this@activity)
    } ?: throw FragmentNotAttachedException()

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        cameraView?.destroy()
        super.onDestroy()
    }

    private fun resumeCamera() {
        cameraView.start()
        CameraViewModel.cameraState = CameraState.NORMAL
        viewModel.clearEmotions()
        cameraView.addFrameProcessor(viewModel::detectFaces)
        cameraButton.setImageDrawable(EmotionDetectorApplication.getDrawable(R.drawable.laughing))
        showProgress(false)
    }

    private fun detectEmotions() {
        cameraView.clearFrameProcessors()
        CameraViewModel.cameraState = CameraState.PRE_PROCESSING
        cameraView.addFrameProcessor { frame ->
            cameraView.clearFrameProcessors()
            viewModel.detectEmotions(frame)
        }
    }

    private fun onEmotionProcessingStarted() {
        cameraView.stop()
        showProgress(true)
        CameraViewModel.cameraState = CameraState.PROCESSING
    }

    private fun onEmotionProcessingFinished() {
        cameraButton.setImageDrawable(EmotionDetectorApplication.getDrawable(R.drawable.ic_done_black_36dp))
        showProgress(false)
        CameraViewModel.cameraState = CameraState.READY
    }

    override fun onEvent(event: Event) = when (event) {
        Event.EMOTION_PROCESSING_STARTED -> onEmotionProcessingStarted()
        Event.EMOTION_PROCESSING_FINISHED -> onEmotionProcessingFinished()
    }
}
