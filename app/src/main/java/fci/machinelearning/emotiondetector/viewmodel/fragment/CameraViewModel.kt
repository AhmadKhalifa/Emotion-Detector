package fci.machinelearning.emotiondetector.viewmodel.fragment

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import fci.machinelearning.emotiondetector.viewmodel.BaseSharedRxViewModel

class CameraViewModel : BaseSharedRxViewModel() {

    companion object {

        @JvmStatic
        fun getInstance(fragmentActivity: FragmentActivity) =
            ViewModelProviders
                .of(fragmentActivity)
                .get(CameraViewModel::class.java)
    }
}