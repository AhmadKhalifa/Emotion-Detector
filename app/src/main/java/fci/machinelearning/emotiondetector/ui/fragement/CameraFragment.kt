package fci.machinelearning.emotiondetector.ui.fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fci.machinelearning.emotiondetector.R
import fci.machinelearning.emotiondetector.ui.base.BaseFragment
import fci.machinelearning.emotiondetector.util.FragmentNotAttachedException
import fci.machinelearning.emotiondetector.viewmodel.fragment.CameraViewModel

class CameraFragment : BaseFragment<CameraViewModel>() {

    companion object {

        val TAG: String = CameraFragment::class.java.simpleName

        fun newInstance() = CameraFragment()
    }

    interface OnFragmentInteractionListener

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_camera, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getViewModelInstance() = activity?.run activity@ {
        CameraViewModel.getInstance(this@activity)
    } ?: throw FragmentNotAttachedException()
}