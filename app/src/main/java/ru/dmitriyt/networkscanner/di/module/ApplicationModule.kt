package ru.dmitriyt.networkscanner.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dmitriyt.networkscanner.presentation.NetworkScannerApplication

@Module
class ApplicationModule {

    @Provides
    fun provideContext(app: NetworkScannerApplication): Context {
        return app.applicationContext
    }

    @Provides
    fun provideApplication(app: NetworkScannerApplication): Application {
        return app
    }
}