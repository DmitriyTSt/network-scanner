package ru.dmitriyt.networkscanner.presentation.navigation

import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment

interface NavigationController {
    fun initNavigation() {
        navigate(getStartFragment(), false)
    }

    fun getStartFragment(): BaseFragment
    fun navigate(fragment: BaseFragment, addToBackStack: Boolean = true)
    fun popBackStack()
}