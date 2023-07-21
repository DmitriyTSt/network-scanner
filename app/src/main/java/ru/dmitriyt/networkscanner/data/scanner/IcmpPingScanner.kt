package ru.dmitriyt.networkscanner.data.scanner

import ru.dmitriyt.networkscanner.data.mapper.NetHostMapper
import ru.dmitriyt.networkscanner.data.mapper.NetUnitMapper
import ru.dmitriyt.networkscanner.domain.model.NetHost
import java.net.InetAddress
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Сканнер по технологии ICMP
 */
class IcmpPingScanner @Inject constructor(
    private val netUnitMapper: NetUnitMapper,
    private val mapper: NetHostMapper,
) {

    /**
     * @return NetHost, если девайс доступен, null иначе
     */
    suspend fun getDeviceByIcmp(addressUInt: UInt, timeout: Int = 1000): NetHost? = suspendCoroutine { continuation ->
        val host = netUnitMapper.uIntToIpv4(addressUInt)
        val device = try {
            InetAddress.getByName(host).let { addr ->
                val isReachable = addr.isReachable(timeout)
                if (isReachable) {
                    mapper.fromSystemToModel(addr, host, addressUInt)
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
        continuation.resume(device)
    }
}