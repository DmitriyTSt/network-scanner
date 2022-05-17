package ru.dmitriyt.networkscanner.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.dmitriyt.networkscanner.presentation.ui.networks.NetworkInterfacesFragment

@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun networkInterfacesFragment(): NetworkInterfacesFragment
}