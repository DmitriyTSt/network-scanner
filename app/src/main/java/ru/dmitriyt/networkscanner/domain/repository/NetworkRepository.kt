package ru.dmitriyt.networkscanner.domain.repository

import ru.dmitriyt.networkscanner.domain.model.NetInterface

interface NetworkRepository {
    suspend fun getNetInterfaces(): List<NetInterface>
    suspend fun getAddressCount(prefix: Short): Int
}