package ru.dmitriyt.networkscanner.data.repository

import ru.dmitriyt.networkscanner.data.mapper.NetworkMapper
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.di.module.DispatcherProvider
import ru.dmitriyt.networkscanner.domain.repository.NetworkRepository
import timber.log.Timber
import java.net.NetworkInterface
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val mapper: NetworkMapper,
) : NetworkRepository {

    /**
     * Получение интерфейсов, отсортированы по активности
     */
    override suspend fun getNetInterfaces(): List<NetInterface> {
        return getNetworkInterfacesList().map { mapper.fromSystemToModel(it) }.sortedBy { !it.isUp }
    }

    /**
     * Получение системных интерфейсов
     */
    private fun getNetworkInterfacesList(): List<NetworkInterface> {
        return try {
            NetworkInterface.getNetworkInterfaces().toList()
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    /**
     * Количество адресов в сети по маске
     */
    override suspend fun getAddressCount(prefix: Short): Int {
        return 2 shl (31 - prefix)
    }
}