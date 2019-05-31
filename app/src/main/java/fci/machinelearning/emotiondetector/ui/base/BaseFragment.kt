package fci.machinelearning.emotiondetector.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import fci.machinelearning.emotiondetector.viewmodel.*

/**
 * @author Ahmad Khalifa
 */

abstract class BaseFragment<out VM : BaseViewModel> : Fragment(), BaseViewModelOwner<VM> {

    private var snackBar: Snackbar? = null

    protected var _rootView: View? = null
    protected val rootView: View
        get() {
            return if (_rootView != null) _rootView as View
            else throw IllegalStateException(
                "Root view cannot be null. " +
                        "It MUST hold the root view created in the onViewCreated method"
            )
        }

    private var _viewModel: VM? = null
    protected val viewModel: VM
        get() {
            _viewModel = _viewModel ?: getViewModelInstance()
            return if (_viewModel != null) _viewModel as VM
            else throw IllegalStateException("getViewModelInstance() implementation returns null!")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _rootView = view
        registerEventHandlerSubscribers(this, viewModel)
        registerLiveDataObservers()
        super.onViewCreated(view, savedInstanceState)
    }

    protected fun snackbar(messageResId: Int) =
        snackbar(context?.resources?.getString(messageResId))

    protected fun snackbar(message: String?): Unit = message?.let {
        snackBar?.dismiss()
        snackBar = Snackbar.make(rootView, it, Snackbar.LENGTH_SHORT)
        snackBar?.show()
    }!!

    protected fun snackbar(newSnackBar: Snackbar?) = newSnackBar?.let {
        snackBar?.dismiss()
        snackBar = it
        snackBar?.show()
    }

    override fun onEvent(event: Event) = snackbar(event.messageRes)

    override fun onError(error: Error) = snackbar(error.messageRes)

    override fun registerLiveDataObservers() {}

    protected fun dismissMessagesIfAny() = snackBar?.dismiss()
}