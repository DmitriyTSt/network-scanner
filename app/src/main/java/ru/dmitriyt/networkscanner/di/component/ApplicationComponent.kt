package ru.dmitriyt.networkscanner.di.component

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import ru.dmitriyt.networkscanner.di.module.ActivityModule
import ru.dmitriyt.networkscanner.di.module.ApplicationModule
import ru.dmitriyt.networkscanner.di.module.CoroutineModule
import ru.dmitriyt.networkscanner.di.module.FragmentModule
import ru.dmitriyt.networkscanner.di.module.RepositoryModule
import ru.dmitriyt.networkscanner.di.module.ViewModelModule
import ru.dmitriyt.networkscanner.presentation.NetworkScannerApplication
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        ActivityModule::class,
        FragmentModule::class,
        ViewModelModule::class,
        CoroutineModule::class,
        RepositoryModule::class,
    ]
)
interface ApplicationComponent : AndroidInjector<NetworkScannerApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: NetworkScannerApplication): ApplicationComponent
    }
}