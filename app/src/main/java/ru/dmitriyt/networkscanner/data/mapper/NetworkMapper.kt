package ru.dmitriyt.networkscanner.data.mapper

import kotlinx.coroutines.withContext
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetHost
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.di.module.DispatcherProvider
import java.net.InetAddress
import java.net.InterfaceAddress
import java.net.NetworkInterface
import javax.inject.Inject
import kotlin.math.pow

class NetworkMapper @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val netUnitMapper: NetUnitMapper,
) {
    suspend fun fromSystemToModel(netInterface: NetworkInterface): NetInterface {
        val ipv4Address = netInterface.getIpAddress(useIPv4 = true)
        val interfaceAddressIpv4 = netInterface.interfaceAddresses.findInterfaceAddress(true)
        return if (ipv4Address != null && interfaceAddressIpv4 != null) {
            NetInterface.Connected(
                name = netInterface.displayName,
                isUp = netInterface.isUp,
                isLoopback = netInterface.isLoopback,
                ipAddress = ipv4Address,
                macAddress = netInterface.hardwareAddress?.let { netUnitMapper.macAddressFromByteArray(it) },
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

    private suspend fun NetworkInterface.getIpAddress(useIPv4: Boolean): String? = withContext(dispatcherProvider.io) {
        inetAddresses.toList().findInetAddress(useIPv4)?.addressToString(useIPv4)
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

    private fun InetAddress.addressToString(useIPv4: Boolean): String? {
        val isIPv4 = isIpv4()
        val sAddr = hostAddress ?: return null
        return if (useIPv4) {
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