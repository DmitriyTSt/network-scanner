package ru.dmitriyt.networkscanner.domain.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.withContext
import ru.dmitriyt.networkscanner.data.mapper.NetHostMapper
import ru.dmitriyt.networkscanner.data.mapper.NetUnitMapper
import ru.dmitriyt.networkscanner.data.scanner.IcmpPingScanner
import ru.dmitriyt.networkscanner.data.scanner.NetBiosScanner
import ru.dmitriyt.networkscanner.di.module.DispatcherProvider
import ru.dmitriyt.networkscanner.domain.model.NetDevice
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.domain.repository.ArpTableRepository
import ru.dmitriyt.networkscanner.domain.repository.NetworkRepository
import ru.dmitriyt.networkscanner.domain.usecase.base.UseCaseFlow
import timber.log.Timber
import java.util.TreeSet
import javax.inject.Inject

/**
 * Получение доступных в сети устройств
 */
class GetDevicesUseCase @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val networkRepository: NetworkRepository,
    private val netUnitMapper: NetUnitMapper,
    private val netHostMapper: NetHostMapper,
    private val arpTableRepository: ArpTableRepository,
    private val icmpPingScanner: IcmpPingScanner,
    private val netBiosScanner: NetBiosScanner,
) : UseCaseFlow<GetDevicesUseCase.Params, List<NetDevice>>() {

    override suspend operator fun invoke(params: Params): Flow<List<NetDevice>> = channelFlow {
        val prefixLength = params.netInterface.prefixLength
        val network = netUnitMapper.ipv4ToUInt(params.netInterface.networkIpAddress)
        val deviceCount = networkRepository.getAddressCount(prefixLength)
        val devices = TreeSet<NetDevice> { d1, d2 -> d1.addressUInt.compareTo(d2.addressUInt) }
        val arpTable = arpTableRepository.getArpTable()

        Timber.d("START SCAN ${this.channel}")
        withContext(dispatcherProvider.io) {
            IntRange(0, deviceCount - 1).map { i ->
                async {
                    val addressUInt = (network + i.toUInt())
                    if (i == 0 || i == deviceCount - 1) {
                        // skip
                    } else {
                        val netHostIcmp = icmpPingScanner.getDeviceByIcmp(addressUInt)
                        val netHostNetBios = netBiosScanner.getDeviceByNetBios(addressUInt)
                        val netHost = (netHostIcmp ?: netHostNetBios)
                            ?.copy(hostName = netHostNetBios?.hostName ?: netHostIcmp?.hostName)
                        if (netHost != null) {
                            val isCurrentDevice = netHost.host == params.netInterface.currentDevice.host
                            val device = if (isCurrentDevice) {
                                params.netInterface.currentDevice
                            } else {
                                netHostMapper.fromHostToDevice(
                                    netHost = netHost,
                                    mac = arpTable[netHost.host]
                                )
                            }
                            devices.add(device)

                            if (!isClosedForSend) {
                                send(devices.toList())
                            }
                        }
                    }
                }
            }.awaitAll()
        }
        Timber.d("END_SCAN")
        val newArpTable = arpTableRepository.getArpTable()
        if (!isClosedForSend) {
            send(
                devices.toList().map {
                    it.copy(
                        mac = if (it.host == params.netInterface.currentDevice.host) {
                            params.netInterface.currentDevice.mac
                        } else {
                            newArpTable[it.host] ?: it.mac
                        }
                    )
                }
            )
        }
    }

    data class Params(
        val netInterface: NetInterface.Connected,
    )
}