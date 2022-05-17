package ru.dmitriyt.networkscanner.presentation.navigation

import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment

interface NavigationController {
    fun getStartFragment(): BaseFragment
    fun navigate(fragment: BaseFragment)
    fun popBackStack()
}