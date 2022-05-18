package ru.dmitriyt.networkscanner.data.repository

import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetInterface

interface NetworkRepository {
    suspend fun getNetInterfaces(): List<NetInterface>
    suspend fun getDevices(netInterface: NetInterface.Connected, timeout: Int = 2000): List<NetDevice>
}