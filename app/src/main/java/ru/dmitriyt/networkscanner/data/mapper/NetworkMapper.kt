package ru.dmitriyt.networkscanner.data.mapper

import ru.dmitriyt.networkscanner.data.model.NetInterface
import java.net.NetworkInterface
import javax.inject.Inject

class NetworkMapper @Inject constructor() {
    fun fromSystemToModel(netInterface: NetworkInterface): NetInterface {
        return NetInterface(
            name = netInterface.displayName,
            ipAddress = "",
            prefixLength = 0,
        )
    }
}