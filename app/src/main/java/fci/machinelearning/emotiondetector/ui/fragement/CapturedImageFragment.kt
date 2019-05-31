package fci.machinelearning.emotiondetector.ui.fragement

import fci.machinelearning.emotiondetector.ui.base.BaseFullScreenDialogFragmentWithSharedViewModel
import fci.machinelearning.emotiondetector.util.FragmentNotAttachedException
import fci.machinelearning.emotiondetector.viewmodel.fragment.CameraViewModel
import fci.machinelearning.emotiondetector.viewmodel.fragment.CapturedImageViewModel

class CapturedImageFragment :
        BaseFullScreenDialogFragmentWithSharedViewModel<CapturedImageViewModel, CameraViewModel>() {



    override fun getViewModelInstance() = CapturedImageViewModel.getInstance(this)

    override fun getSharedViewModelInstance() = activity?.run activity@ {
        CameraViewModel.getInstance(this@activity)
    } ?: throw FragmentNotAttachedException()
}