package ru.dmitriyt.networkscanner.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
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
    private var scanDevicesJob: Job? = null

    override suspend fun getNetInterfaces(): List<NetInterface> {
        return getNetworkInterfacesList().map { mapper.fromSystemToModel(it) }.sortedBy { !it.isUp }
    }

    override suspend fun getDevices(
        netInterface: NetInterface.Connected,
        timeout: Int
    ): Flow<List<NetDevice>> {
        return getDevices(mapper.ipv4ToUInt(netInterface.networkIpAddress), netInterface.prefixLength, timeout)
    }

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
    private fun getAddressCount(prefix: Short): Int {
        return 2 shl (31 - prefix)
    }

    /**
     * Получить доступные девайсы в сети
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getDevices(
        network: UInt,
        prefixLength: Short,
        timeout: Int,
    ): Flow<List<NetDevice>> = channelFlow {
        val deviceCount = getAddressCount(prefixLength)
        val devices = mutableListOf<NetDevice>()

        Timber.d("START SCAN ${this.channel}")
        IntRange(0, deviceCount - 1).map { i ->
            async(dispatcherProvider.io) {
                val addressUInt = (network + i.toUInt())
                val host = mapper.uIntToIpv4(addressUInt)
                if (i == 0 || i == deviceCount - 1) {
                    // skip
                } else {
                    val isReachableResult = isReachable(host, timeout)
                    if (isReachableResult is IsReachableResult.Exist) {
                        devices.add(isReachableResult.device)
                        if (!isClosedForSend) {
                            Timber.d("SEND DEVICE ${isReachableResult.device.host}")
                            send(devices)
                        }
                    } else {
                        Timber.d("$host not exists")
                    }
                }
            }
        }.awaitAll()
        Timber.d("END_SCAN")
    }

    /**
     * Доступен ли хост
     */
    private suspend fun isReachable(host: String, timeout: Int): IsReachableResult = suspendCoroutine { continuation ->
        val isReachable = try {
            InetAddress.getByName(host).let { addr ->
                val isReachable = addr.isReachable(timeout)
                if (isReachable) {
                    Timber.d("IS REACHABLE $host")
                    IsReachableResult.Exist(NetDevice(host, addr.hostName.takeIf { it != host }))
                } else {
                    IsReachableResult.NotExist
                }
            }
        } catch (e: Exception) {
            IsReachableResult.NotExist
        }
        continuation.resume(isReachable)
    }
}