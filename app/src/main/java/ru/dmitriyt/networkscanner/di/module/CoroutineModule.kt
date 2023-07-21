package ru.dmitriyt.networkscanner.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
abstract class CoroutineModule {
    @Binds
    abstract fun provideDispatcherProvider(dispatcherProvider: DispatcherProviderImpl): DispatcherProvider
}

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}

class DispatcherProviderImpl @Inject constructor() : DispatcherProvider {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default

}