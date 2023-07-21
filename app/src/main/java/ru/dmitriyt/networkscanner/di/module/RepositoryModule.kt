package ru.dmitriyt.networkscanner.di.module

import dagger.Binds
import dagger.Module
import ru.dmitriyt.networkscanner.domain.repository.ArpTableRepository
import ru.dmitriyt.networkscanner.data.repository.ArpTableRepositoryImpl
import ru.dmitriyt.networkscanner.domain.repository.NetworkRepository
import ru.dmitriyt.networkscanner.data.repository.NetworkRepositoryImpl

@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideNetworkRepository(networkRepository: NetworkRepositoryImpl): NetworkRepository

    @Binds
    abstract fun provideArpTableRepository(arpTableRepository: ArpTableRepositoryImpl): ArpTableRepository
}