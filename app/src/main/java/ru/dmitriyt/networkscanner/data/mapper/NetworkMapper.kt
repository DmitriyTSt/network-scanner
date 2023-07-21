package ru.dmitriyt.networkscanner.data.mapper

import kotlinx.coroutines.withContext
import ru.dmitriyt.networkscanner.di.module.DispatcherProvider
import ru.dmitriyt.networkscanner.domain.model.NetDevice
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import java.net.InetAddress
import java.net.InterfaceAddress
import java.net.NetworkInterface
import javax.inject.Inject

class NetworkMapper @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val netUnitMapper: NetUnitMapper,
) {
    suspend fun fromSystemToModel(netInterface: NetworkInterface): NetInterface = withContext(dispatcherProvider.io) {
        val ipv4InetAddress = netInterface.getInetAddress(useIPv4 = true)
        val ipv4Address = ipv4InetAddress?.addressToString(useIPv4 = true)
        val interfaceAddressIpv4 = netInterface.interfaceAddresses.findInterfaceAddress(true)
        if (ipv4InetAddress != null && ipv4Address != null && interfaceAddressIpv4 != null) {
            NetInterface.Connected(
                name = netInterface.displayName,
                isUp = netInterface.isUp,
                isLoopback = netInterface.isLoopback,
                currentDevice = NetDevice(
                    host = ipv4Address,
                    hostName = ipv4InetAddress.hostName.takeIf { it != ipv4Address },
                    addressUInt = netUnitMapper.ipv4ToUInt(ipv4Address),
                    isCurrentDevice = true,
                    mac = netInterface.hardwareAddress?.let { netUnitMapper.macAddressFromByteArray(it) },
                ),
                prefixLength = interfaceAddressIpv4.networkPrefixLength,
                networkIpAddress = netUnitMapper.uIntToIpv4(
                    getNetworkFromAddressAndMask(
                        ipv4Address,
                        interfaceAddressIpv4.networkPrefixLength,
                    )
                ),
            )
        } else {
            NetInterface.Disconnected(
                name = netInterface.displayName,
                isUp = netInterface.isUp,
                isLoopback = netInterface.isLoopback,
            )
        }
    }

    /**
     * Получить сеть по любому адресу и маске
     */
    private fun getNetworkFromAddressAndMask(ipv4Address: String, prefixLength: Short): UInt {
        return netUnitMapper.ipv4ToUInt(ipv4Address) and netUnitMapper.prefixLengthToInt(prefixLength)
    }

    private suspend fun NetworkInterface.getInetAddress(useIPv4: Boolean): InetAddress? = withContext(dispatcherProvider.io) {
        inetAddresses.toList().findInetAddress(useIPv4)
    }

    private fun List<InterfaceAddress>.findInterfaceAddress(useIPv4: Boolean): InterfaceAddress? {
        return find {
            if (useIPv4) {
                it.address.isIpv4()
            } else {
                it.address.isIpv6()
            }
        }
    }

    private fun List<InetAddress>.findInetAddress(useIPv4: Boolean): InetAddress? {
        return find {
            if (useIPv4) {
                it.isIpv4()
            } else {
                it.isIpv6()
            }
        }
    }

    private fun InetAddress.isIpv4(): Boolean {
        return (hostAddress?.indexOf(':') ?: 0) < 0
    }

    private fun InetAddress.isIpv6(): Boolean {
        return (hostAddress?.indexOf(':') ?: -1) >= 0
    }

    private suspend fun InetAddress.addressToString(useIPv4: Boolean): String? = withContext(dispatcherProvider.io) {
        val isIPv4 = isIpv4()
        val sAddr = hostAddress ?: return@withContext null
        if (useIPv4) {
            if (isIPv4) sAddr else null
        } else {
            if (!isIPv4) {
                val separator = sAddr.indexOf('%') // drop ip6 zone suffix
                if (separator < 0) sAddr.uppercase() else sAddr.substring(0, separator).uppercase()
            } else {
                null
            }
        }
    }
}