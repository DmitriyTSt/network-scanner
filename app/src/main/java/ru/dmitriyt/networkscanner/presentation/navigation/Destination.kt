package ru.dmitriyt.networkscanner.presentation.navigation

import android.content.Intent
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment

sealed class Destination {
    class Activity(val intent: Intent) : Destination()
    class Fragment(val fragment: BaseFragment) : Destination()
    class Stack(vararg val destinations: Destination) : Destination()
    object Back : Destination()
}