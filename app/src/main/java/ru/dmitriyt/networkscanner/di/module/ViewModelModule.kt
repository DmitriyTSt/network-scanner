package ru.dmitriyt.networkscanner.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.dmitriyt.networkscanner.di.module.viewmodel.ViewModelFactoryModule
import ru.dmitriyt.networkscanner.di.util.ViewModelKey
import ru.dmitriyt.networkscanner.presentation.ui.network.NetworkViewModel
import ru.dmitriyt.networkscanner.presentation.ui.networks.NetworkInterfacesViewModel

@Module(includes = [ViewModelFactoryModule::class])
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(NetworkInterfacesViewModel::class)
    abstract fun networkInterfacesViewModel(viewModel: NetworkInterfacesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NetworkViewModel::class)
    abstract fun networkViewModel(viewModel: NetworkViewModel): ViewModel
}