package ru.dmitriyt.networkscanner.presentation

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import ru.dmitriyt.networkscanner.di.component.DaggerApplicationComponent

class NetworkScannerApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .factory()
            .create(this)
    }
}