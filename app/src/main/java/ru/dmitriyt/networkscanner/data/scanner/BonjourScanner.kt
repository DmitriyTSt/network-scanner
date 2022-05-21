package ru.dmitriyt.networkscanner.data.scanner

import ru.dmitriyt.networkscanner.data.mapper.NetHostMapper
import ru.dmitriyt.networkscanner.data.mapper.NetUnitMapper
import ru.dmitriyt.networkscanner.data.model.NetHost
import java.net.InetAddress
import javax.inject.Inject
import javax.jmdns.impl.HostInfo
import javax.jmdns.impl.JmDNSImpl
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Сканнер по технологии Bonjour
 */
class BonjourScanner @Inject constructor(
    private val netUnitMapper: NetUnitMapper,
    private val mapper: NetHostMapper,
) {

    /**
     * @return NetHost, если девайс доступен, null иначе
     */
    suspend fun getDeviceByBonjour(addressUInt: UInt): NetHost? = suspendCoroutine { continuation ->
        val host = netUnitMapper.uIntToIpv4(addressUInt)
        val jmdns = try {
            JmDNSImpl(null, null)
        } catch (e: Exception) {
            null
        }
        val device = try {
            HostInfo.newHostInfo(InetAddress.getByName(host), jmdns, null).inetAddress.let { addr ->
                mapper.fromSystemToModel(addr, host, addressUInt)
                    .takeIf { it.hostName != null }
            }
        } catch (e: Exception) {
            null
        } finally {
            jmdns?.close()
        }
        continuation.resume(device)
    }
}