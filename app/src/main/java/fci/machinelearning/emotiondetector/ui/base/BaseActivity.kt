package fci.machinelearning.emotiondetector.ui.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import fci.machinelearning.emotiondetector.util.toast
import fci.machinelearning.emotiondetector.viewmodel.*

/**
 * @author Ahmad Khalifa
 */

@SuppressLint("Registered")
abstract class BaseActivity<out VM : BaseViewModel> :
    AppCompatActivity(),
    BaseViewModelOwner<VM> {

    private var _viewModel: VM? = null

    protected val viewModel: VM
        get() {
            _viewModel = _viewModel ?: getViewModelInstance()
            return if (_viewModel != null) _viewModel as VM
            else throw IllegalStateException("getViewModelInstance() implementation returns null!")
        }

    override fun onResume() {
        super.onResume()
        registerEventHandlerSubscribers(this, viewModel)
        registerLiveDataObservers()
    }

    override fun onEvent(event: Event) = toast(event.messageRes)

    override fun onError(error: Error) = toast(error.messageRes)

    override fun registerLiveDataObservers() {}
}