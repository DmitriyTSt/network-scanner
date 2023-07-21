package ru.dmitriyt.networkscanner.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import ru.dmitriyt.networkscanner.data.repository.ArpTableRepositoryImpl
import ru.dmitriyt.networkscanner.data.repository.NetworkRepositoryImpl
import ru.dmitriyt.networkscanner.data.repository.ResourceRepositoryImpl
import ru.dmitriyt.networkscanner.domain.repository.ArpTableRepository
import ru.dmitriyt.networkscanner.domain.repository.NetworkRepository
import ru.dmitriyt.networkscanner.domain.repository.ResourceRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideNetworkRepository(networkRepository: NetworkRepositoryImpl): NetworkRepository

    @Binds
    abstract fun provideArpTableRepository(arpTableRepository: ArpTableRepositoryImpl): ArpTableRepository

    @Binds
    abstract fun provideResourceRepository(resourceRepository: ResourceRepositoryImpl): ResourceRepository
}