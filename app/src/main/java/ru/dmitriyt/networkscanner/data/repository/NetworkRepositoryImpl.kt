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

    override suspend fun getNetInterfaces(): List<NetInterface> {
        return getNetworkInterfacesList().map { mapper.fromSystemToModel(it) }.sortedBy { !it.isUp }
    }

    override suspend fun getDevices(
        netInterface: NetInterface.Connected,
        timeout: Int
    ): Flow<List<NetDevice>> {
        return getDevices(
            currentHost = netInterface.ipAddress,
            network = mapper.ipv4ToUInt(netInterface.networkIpAddress),
            prefixLength = netInterface.prefixLength,
            timeout = timeout
        )
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
     * Получить доступные девайсы в сети по Inet
     */
    private suspend fun getDevices(
        currentHost: String,
        network: UInt,
        prefixLength: Short,
        timeout: Int,
    ): Flow<List<NetDevice>> = channelFlow {
        val deviceCount = getAddressCount(prefixLength)
        val devices = mutableListOf<NetDevice>()
        val arpTable = getArpTable()

        Timber.d("START SCAN ${this.channel}")
        IntRange(0, deviceCount - 1).map { i ->
            async(dispatcherProvider.io) {
                val addressUInt = (network + i.toUInt())
                val host = mapper.uIntToIpv4(addressUInt)
                if (i == 0 || i == deviceCount - 1) {
                    // skip
                } else {
                    val device = getDeviceByInet(host, timeout)
                        ?: getDeviceByNetBios(host)
                        ?: getDeviceByBonjour(host)
                    if (device != null) {
                        devices.add(
                            device.copy(
                                mac = arpTable[device.host],
                                isCurrentDevice = device.host == currentHost,
                            )
                        )
                        if (!isClosedForSend) {
                            Timber.d("SEND DEVICE ${device.host}")
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
     * Доступен ли хост по Inet
     */
    private suspend fun getDeviceByInet(host: String, timeout: Int): NetDevice? = suspendCoroutine { continuation ->
        val device = try {
            InetAddress.getByName(host).let { addr ->
                val isReachable = addr.isReachable(timeout)
                if (isReachable) {
                    Timber.d("IS REACHABLE $host")
                    mapper.fromInetAddressToDevice(addr, host)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
        continuation.resume(device)
    }

    /**
     * Доступен ли хост по NetBios
     */
    private suspend fun getDeviceByNetBios(host: String): NetDevice? = suspendCoroutine { continuation ->
        val device = try {
            NbtAddress.getByName(host).inetAddress.let { addr ->
                mapper.fromInetAddressToDevice(addr, host)
                    .takeIf { it.hostName != null }
            }
        } catch (e: Exception) {
            null
        }
        continuation.resume(device)
    }

    /**
     * Доступен ли хост по Bonjour
     */
    private suspend fun getDeviceByBonjour(host: String): NetDevice? = suspendCoroutine { continuation ->
        val jmdns = try {
            JmDNSImpl(null, null)
        } catch (e: Exception) {
            null
        }
        val device = try {
            HostInfo.newHostInfo(InetAddress.getByName(host), jmdns, null).inetAddress.let { addr ->
                mapper.fromInetAddressToDevice(addr, host)
                    .takeIf { it.hostName != null }
            }
        } catch (e: Exception) {
            null
        } finally {
            jmdns?.close()
        }
        continuation.resume(device)
    }

    private val macRegex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$".toRegex()

    private suspend fun getArpTable(): Map<String, String> = withContext(dispatcherProvider.io) {
        suspendCoroutine { continuation ->
            var bufferedReader: BufferedReader? = null
            val result = mutableMapOf<String, String>()
            try {
                bufferedReader = BufferedReader(FileReader("/proc/net/arp"))
                var line: String? = bufferedReader.readLine()
                Timber.d("ARP_TABLE_ROW FIRST_LINE $line")
                while (line != null) {
                    Timber.d("ARP_TABLE_ROW LINE $line")
                    val splitted = line.split("\\s+".toRegex()).toTypedArray()
                    if (splitted.size >= 4) {
                        val ip = splitted[0]
                        val mac = splitted[3]
                        Timber.d("ARP_TABLE_ROW $ip $mac")
                        if (mac.matches(macRegex)) {
                            result[ip] = mac.uppercase()
                        }
                    }
                    line = bufferedReader.readLine()
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    bufferedReader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            continuation.resume(result)
        }
    }
}