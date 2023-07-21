package ru.dmitriyt.networkscanner.presentation.navigation

import androidx.fragment.app.Fragment
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseViewModel

fun Fragment.observeNavigationCommands(viewModel: BaseViewModel) {
    viewModel.destinationLiveEvent.observe(viewLifecycleOwner) { destination ->
        processDestination(destination)
    }
}

private fun Fragment.processDestination(destination: Destination) {
    when (destination) {
        is Destination.Fragment -> findNavController().navigate(destination.fragment)
        is Destination.Back -> findNavController().popBackStack()
        is Destination.Activity -> startActivity(destination.intent)
        is Destination.Stack -> {
            destination.destinations.forEach { processDestination(it) }
        }
    }
}

private fun Fragment.findNavController(): NavigationController {
    var currentParentFragment = parentFragment
    while (currentParentFragment != null) {
        if (currentParentFragment is NavigationController) {
            return currentParentFragment
        } else {
            currentParentFragment = currentParentFragment.parentFragment
        }
    }
    if (activity is NavigationController) {
        return activity as NavigationController
    }
    throw IllegalStateException("NavigationController not found")
}