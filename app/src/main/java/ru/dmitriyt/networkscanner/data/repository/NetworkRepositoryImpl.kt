package ru.dmitriyt.networkscanner.data.repository

import jcifs.netbios.NbtAddress
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import ru.dmitriyt.networkscanner.data.mapper.NetworkMapper
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.di.module.DispatcherProvider
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.IOException
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject
import javax.jmdns.impl.HostInfo
import javax.jmdns.impl.JmDNSImpl
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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