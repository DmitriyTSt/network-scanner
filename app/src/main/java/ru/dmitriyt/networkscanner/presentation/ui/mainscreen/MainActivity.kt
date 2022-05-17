package ru.dmitriyt.networkscanner.presentation.ui.mainscreen

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.databinding.ActivityMainBinding
import ru.dmitriyt.networkscanner.presentation.navigation.NavigationController
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseActivity
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseFragment
import ru.dmitriyt.networkscanner.presentation.ui.networks.NetworkInterfacesFragment

class MainActivity : BaseActivity(), NavigationController {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigate(getStartFragment())
    }

    override fun getStartFragment() = NetworkInterfacesFragment.newInstance()

    override fun navigate(fragment: BaseFragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainerView.id, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun popBackStack() {
        supportFragmentManager.popBackStack()
    }
}