package ru.dmitriyt.networkscanner.presentation

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.dmitriyt.networkscanner.data.repository.NetworkRepository
import ru.dmitriyt.networkscanner.di.component.DaggerApplicationComponent
import timber.log.Timber
import javax.inject.Inject

class NetworkScannerApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .factory()
            .create(this)
    }

    @Inject lateinit var networkRepository: NetworkRepository

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}