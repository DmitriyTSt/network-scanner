package ru.dmitriyt.networkscanner.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dmitriyt.networkscanner.data.DiffUtilCallbackFactory
import ru.dmitriyt.networkscanner.data.DiffUtilItemCallbackFactory
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

    @Provides
    fun provideDiffUtilItemCallbackFactory(): DiffUtilItemCallbackFactory {
        return DiffUtilItemCallbackFactory()
    }

    @Provides
    fun provideDiffUtilCallbackFactory(
        diffUtilItemCallbackFactory: DiffUtilItemCallbackFactory,
    ): DiffUtilCallbackFactory {
        return DiffUtilCallbackFactory(diffUtilItemCallbackFactory)
    }
}