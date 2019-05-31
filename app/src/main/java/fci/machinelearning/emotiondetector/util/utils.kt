package fci.machinelearning.emotiondetector.util

import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import fci.machinelearning.emotiondetector.EmotionDetectorApplication
import fci.machinelearning.emotiondetector.EmotionDetectorApplication.Companion.getString
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

private var toast: Toast? = null

fun toast(@StringRes messageResId: Int) = showToast(getString(messageResId), Toast.LENGTH_SHORT)

fun toast(message: String?) = showToast(message, Toast.LENGTH_SHORT)

fun longToast(@StringRes messageResId: Int) = showToast(getString(messageResId), Toast.LENGTH_LONG)

fun longToast(message: String?) = showToast(message, Toast.LENGTH_LONG)

private fun showToast(message: String?, duration: Int) {
    toast?.cancel()
    toast = Toast.makeText(EmotionDetectorApplication.instance, "$message", duration)
    toast?.show()
}

inline fun <reified C: Any, T> C.performAsync(
    crossinline action: () -> T?,
    noinline onSuccess: (T?) -> Unit = {},
    noinline onFailure: (Throwable) -> Unit = {}
): Disposable = Flowable.fromCallable<T> {
    try {
        action()
    } catch (exception: Exception) {
        exception.printStackTrace()
        throw Exception(exception)
    }
}.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess, onFailure)

inline fun <reified T: Any> T.info(message: String?) =
    Log.i(T::class.java.simpleName, message ?: "")

inline fun <reified T: Any> T.debug(message: String?) =
    Log.d(T::class.java.simpleName, message ?: "")

inline fun <reified T: Any> T.warn(message: String?) =
    Log.w(T::class.java.simpleName, message ?: "")

inline fun <reified T: Any> T.error(message: String?, throwable: Throwable? = null) =
    Log.e(T::class.java.simpleName, message ?: "", throwable)

inline fun <reified T: Any> T.verbose(message: String?) = Log.v(T::class.java.simpleName, message ?: "")

inline fun <reified T: Any> T.wtf(message: String?) = Log.wtf(T::class.java.simpleName, message ?: "")