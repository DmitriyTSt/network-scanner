package ru.dmitriyt.networkscanner.data.scanner

import jcifs.netbios.NbtAddress
import ru.dmitriyt.networkscanner.data.mapper.NetHostMapper
import ru.dmitriyt.networkscanner.data.mapper.NetUnitMapper
import ru.dmitriyt.networkscanner.domain.model.NetHost
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Сканнер по технологии NetBIOS
 */
class NetBiosScanner @Inject constructor(
    private val netUnitMapper: NetUnitMapper,
    private val mapper: NetHostMapper,
) {

    /**
     * @return NetHost, если девайс доступен, null иначе
     */
    suspend fun getDeviceByNetBios(addressUInt: UInt): NetHost? = suspendCoroutine { continuation ->
        val host = netUnitMapper.uIntToIpv4(addressUInt)
        val device = try {
            NbtAddress.getByName(host).inetAddress.let { addr ->
                mapper.fromSystemToModel(addr, host, addressUInt)
                    .takeIf { it.hostName != null }
            }
        } catch (e: Exception) {
            null
        }
        continuation.resume(device)
    }
}