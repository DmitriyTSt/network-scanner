package ru.dmitriyt.networkscanner.data.repository

import ru.dmitriyt.networkscanner.data.model.NetInterface

interface NetworkRepository {
    suspend fun getNetInterfaces(): List<NetInterface>
}