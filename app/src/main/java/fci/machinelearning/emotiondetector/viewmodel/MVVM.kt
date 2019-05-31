package fci.machinelearning.emotiondetector.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * @author Ahmad Khalifa
 */

open class BaseViewModel : ViewModel() {

    val event = MutableLiveData<Event>()
    val error = MutableLiveData<Error>()

    protected fun notify(event: Event) {
        this.event.value = event
    }

    protected fun notify(error: Error) {
        this.error.value = error
    }
}

open class BaseRxViewModel : BaseViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}

interface IViewModel

interface ISharedViewModel

open class BaseSharedRxViewModel : BaseRxViewModel()

enum class Event(@StringRes val messageRes: Int)

enum class Error(@StringRes val messageRes: Int)

interface BaseViewModelOwner<out VM : BaseViewModel> {

    fun registerEventHandlerSubscribers(
        lifecycleOwner: LifecycleOwner,
        viewModel: BaseViewModel
    ) {
        viewModel.event.observe(
            lifecycleOwner,
            Observer { event -> event?.let(::onEvent) }
        )
        viewModel.error.observe(
            lifecycleOwner,
            Observer { error -> error?.let(::onError) }
        )
    }

    fun getViewModelInstance(): VM

    fun onEvent(event: Event): Unit?

    fun onError(error: Error): Unit?

    fun registerLiveDataObservers()
}

interface BaseSharedViewModelOwner<out SVM : BaseSharedRxViewModel> {

    fun registerSharedVMEventHandlerSubscribers(
        lifecycleOwner: LifecycleOwner,
        sharedRxViewModel: BaseSharedRxViewModel
    ) {
        sharedRxViewModel.event.observe(
            lifecycleOwner,
            Observer { event -> event?.let(::onEvent) }
        )
        sharedRxViewModel.error.observe(
            lifecycleOwner,
            Observer { error -> error?.let(::onError) }
        )
    }

    fun getSharedViewModelInstance(): SVM

    fun onEvent(event: Event): Unit?

    fun onError(error: Error): Unit?
}