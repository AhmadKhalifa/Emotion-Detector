package fci.machinelearning.emotiondetector.viewmodel.fragment

import androidx.lifecycle.ViewModelProviders
import fci.machinelearning.emotiondetector.ui.fragement.CapturedImageFragment
import fci.machinelearning.emotiondetector.viewmodel.BaseRxViewModel

class CapturedImageViewModel : BaseRxViewModel() {

    companion object {

        @JvmStatic
        fun getInstance(capturedImageFragment: CapturedImageFragment) =
            ViewModelProviders
                .of(capturedImageFragment)
                .get(CapturedImageViewModel::class.java)
    }
}