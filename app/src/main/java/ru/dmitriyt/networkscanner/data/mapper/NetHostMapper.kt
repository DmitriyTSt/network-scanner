package ru.dmitriyt.networkscanner.data.mapper

import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetHost
import java.net.InetAddress
import javax.inject.Inject

class NetHostMapper @Inject constructor() {
    fun fromSystemToModel(inetAddress: InetAddress, host: String, addressUInt: UInt): NetHost {
        return NetHost(
            host = host,
            addressUInt = addressUInt,
            hostName = inetAddress.hostName.takeIf { it != host },
        )
    }

    fun fromHostToDevice(netHost: NetHost, isCurrentDevice: Boolean, mac: String?): NetDevice {
        return NetDevice(
            host = netHost.host,
            addressUInt = netHost.addressUInt,
            hostName = netHost.hostName,
            isCurrentDevice = isCurrentDevice,
            mac = mac,
        )
    }
}