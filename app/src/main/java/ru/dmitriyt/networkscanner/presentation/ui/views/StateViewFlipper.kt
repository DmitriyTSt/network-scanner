package ru.dmitriyt.networkscanner.presentation.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ViewFlipper
import androidx.annotation.StringRes
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.presentation.model.LoadableState
import ru.dmitriyt.networkscanner.databinding.ViewErrorStateBinding
import ru.dmitriyt.networkscanner.databinding.ViewLoadingStateBinding

class StateViewFlipper(context: Context, attrs: AttributeSet? = null) : ViewFlipper(context, attrs) {

    enum class State(val displayedChild: Int) {
        LOADING(0),
        ERROR(1),
        DATA(2),
        CUSTOM(3)
    }

    private val loadingBinding = ViewLoadingStateBinding.inflate(LayoutInflater.from(context), this, true)
    private val errorBinding = ViewErrorStateBinding.inflate(LayoutInflater.from(context), this, true)

    fun <T> setState(loadableResult: LoadableState<T>) {
        when (loadableResult) {
            is LoadableState.Loading -> setStateLoading()
            is LoadableState.Success -> setStateData()
            is LoadableState.Error -> setStateError(loadableResult.throwable)
        }
    }

    fun setRetryMethod(retry: () -> Unit) {
        errorBinding.buttonRepeat.setOnClickListener { retry.invoke() }
    }

    private fun setStateLoading() {
        displayedChild = State.LOADING.displayedChild
    }

    private fun setStateData() {
        displayedChild = State.DATA.displayedChild
    }

    private fun setStateError(error: Throwable) {
        displayedChild = State.ERROR.displayedChild
        setGeneralError(error.message)
    }

    private fun setGeneralError(message: String?) {
        setErrorStateContent(
            titleRes = R.string.error_something_wrong_title,
            description = message ?: context.getString(R.string.error_something_wrong_description),
        )
    }

    private fun setErrorStateContent(@StringRes titleRes: Int, description: String) = with(errorBinding) {
        textViewErrorTitle.setText(titleRes)
        textViewErrorMessage.text = description
    }
}