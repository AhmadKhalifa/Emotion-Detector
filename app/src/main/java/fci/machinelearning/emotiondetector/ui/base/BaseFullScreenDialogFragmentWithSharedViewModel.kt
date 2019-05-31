package fci.machinelearning.emotiondetector.ui.base

import android.os.Bundle
import android.view.View
import fci.machinelearning.emotiondetector.viewmodel.BaseRxViewModel
import fci.machinelearning.emotiondetector.viewmodel.BaseSharedRxViewModel
import fci.machinelearning.emotiondetector.viewmodel.BaseSharedViewModelOwner

/**
 * @author Ahmad Khalifa
 */

abstract class
BaseFullScreenDialogFragmentWithSharedViewModel<out VM : BaseRxViewModel, out SVM: BaseSharedRxViewModel> :
    BaseFullScreenDialogFragment<VM>(),
    BaseSharedViewModelOwner<SVM> {

    private var _sharedViewModel: SVM? = null
    protected val sharedViewModel: SVM
        get() {
            _sharedViewModel = _sharedViewModel ?: getSharedViewModelInstance()
            return if (_sharedViewModel != null) _sharedViewModel as SVM
            else throw IllegalStateException(
                "getSharedViewModelInstance() implementation returns null!"
            )
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerSharedVMEventHandlerSubscribers(this, sharedViewModel)
        super.onViewCreated(view, savedInstanceState)
    }
}