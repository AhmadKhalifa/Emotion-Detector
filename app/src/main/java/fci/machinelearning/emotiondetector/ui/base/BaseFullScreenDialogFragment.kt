package fci.machinelearning.emotiondetector.ui.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import fci.machinelearning.emotiondetector.R
import fci.machinelearning.emotiondetector.util.toast
import fci.machinelearning.emotiondetector.viewmodel.BaseViewModel
import fci.machinelearning.emotiondetector.viewmodel.BaseViewModelOwner
import fci.machinelearning.emotiondetector.viewmodel.Error
import fci.machinelearning.emotiondetector.viewmodel.Event

/**
 * @author Ahmad Khalifa
 */

abstract class BaseFullScreenDialogFragment<out VM : BaseViewModel> :
    DialogFragment(),
    BaseViewModelOwner<VM> {

    private var _viewModel: VM? = null

    protected val viewModel: VM
        get() {
            _viewModel = _viewModel ?: getViewModelInstance()
            return if (_viewModel != null) _viewModel as VM
            else throw IllegalStateException("getViewModelInstance() implementation returns null!")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(R.style.DialogAnimation)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerEventHandlerSubscribers(this, viewModel)
        registerLiveDataObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onEvent(event: Event) = toast(event.messageRes)

    override fun onError(error: Error) = toast(error.messageRes)

    override fun registerLiveDataObservers() {}
}