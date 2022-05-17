package ru.dmitriyt.networkscanner.di.module

import dagger.Binds
import dagger.Module
import ru.dmitriyt.networkscanner.data.repository.NetworkRepository
import ru.dmitriyt.networkscanner.data.repository.NetworkRepositoryImpl

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideNetworkRepository(networkRepository: NetworkRepositoryImpl): NetworkRepository
}