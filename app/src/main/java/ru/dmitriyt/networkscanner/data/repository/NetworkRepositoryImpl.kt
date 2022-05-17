package ru.dmitriyt.networkscanner.data.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ru.dmitriyt.networkscanner.data.mapper.NetworkMapper
import ru.dmitriyt.networkscanner.data.model.IsReachableResult
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.di.module.DispatcherProvider
import timber.log.Timber
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NetworkRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val mapper: NetworkMapper,
) : NetworkRepository {
    override suspend fun getNetInterfaces(): List<NetInterface> {
        return getNetworkInterfacesList().map { mapper.fromSystemToModel(it) }
    }

    override suspend fun getDevices(netInterface: NetInterface.Connected, timeout: Int): List<NetDevice> {
        return getDevices(getNetwork(netInterface.ipAddress, netInterface.prefixLength), netInterface.prefixLength, timeout)
    }

    private fun getNetworkInterfacesList(): List<NetworkInterface> {
        return try {
            NetworkInterface.getNetworkInterfaces().toList().filter { it.isUp && !it.isLoopback }
        } catch (e: Exception) {
            Timber.e(e)
            emptyList()
        }
    }

    /**
     * Количество адресов в сети по маске
     */
    private fun getAddressCount(prefix: Short): Int {
        return 2 shl (31 - prefix)
    }

    /**
     * Получить сеть по любому адресу и маске
     */
    private fun getNetwork(ipv4Address: String, prefixLength: Short): UInt {
        return mapper.ipv4ToUInt(ipv4Address) and mapper.prefixLengthToInt(prefixLength)
    }

    /**
     * Получить доступные девайсы в сети
     */
    private suspend fun getDevices(
        network: UInt,
        prefixLength: Short,
        timeout: Int,
    ): List<NetDevice> = withContext(dispatcherProvider.io) {
        val deviceCount = getAddressCount(prefixLength)

        IntRange(0, deviceCount - 1).map { i ->
            val addressUInt = (network + i.toUInt())
            val host = mapper.intToIpv4(addressUInt)
            Timber.d("CHECK $host")

            if (i == 0 || i == deviceCount - 1) return@map async { IsReachableResult.NotExist }

            async { isReachable(host, timeout) }
        }.awaitAll().filterIsInstance<IsReachableResult.Exist>().map { it.device }
    }

    /**
     * Доступен ли хост
     */
    private suspend fun isReachable(host: String, timeout: Int): IsReachableResult = suspendCoroutine {
        val isReachable = try {
            InetAddress.getByName(host).let { addr ->
                val isReachable = addr.isReachable(timeout)
                if (isReachable) {
                    IsReachableResult.Exist(NetDevice(host, addr.hostName))
                } else {
                    IsReachableResult.NotExist
                }
            }
        } catch (e: Exception) {
            IsReachableResult.NotExist
        }
        it.resume(isReachable)
    }
}