package fci.machinelearning.emotiondetector.viewmodel.activity

import androidx.lifecycle.ViewModelProviders
import fci.machinelearning.emotiondetector.ui.activity.CameraActivity
import fci.machinelearning.emotiondetector.viewmodel.BaseRxViewModel

class CameraActivityViewModel : BaseRxViewModel() {

    companion object {

        fun getInstance(cameraActivity: CameraActivity) =
            ViewModelProviders.of(cameraActivity).get(CameraActivityViewModel::class.java)
    }
}