package ru.dmitriyt.networkscanner.data.repository

import kotlinx.coroutines.flow.Flow
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetInterface

interface NetworkRepository {
    suspend fun getNetInterfaces(): List<NetInterface>
    suspend fun getDevices(netInterface: NetInterface.Connected, timeout: Int = 1000): Flow<List<NetDevice>>
}