package ru.dmitriyt.networkscanner.presentation.extensions

import androidx.annotation.MainThread
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModel
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseActivity
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment

@MainThread
inline fun <reified VM : ViewModel> BaseFragment.appViewModels() =
    createViewModelLazy(VM::class, { this.viewModelStore }, { viewModelFactory })

@MainThread
inline fun <reified VM : ViewModel> BaseFragment.appActivityViewModels() =
    createViewModelLazy(
        VM::class,
        { requireActivity().viewModelStore },
        {
            if (requireActivity() is BaseActivity) {
                (requireActivity() as BaseActivity).viewModelFactory
            } else {
                requireActivity().defaultViewModelProviderFactory
            }
        }
    )