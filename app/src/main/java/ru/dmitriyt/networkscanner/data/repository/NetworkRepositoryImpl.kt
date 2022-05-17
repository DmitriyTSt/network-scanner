package ru.dmitriyt.networkscanner.data.repository

import ru.dmitriyt.networkscanner.data.mapper.NetworkMapper
import ru.dmitriyt.networkscanner.data.model.NetInterface
import java.net.NetworkInterface
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val mapper: NetworkMapper,
) : NetworkRepository {
    override suspend fun getNetInterfaces(): List<NetInterface> {
        return try {
            NetworkInterface.getNetworkInterfaces().toList().filter { it.isUp && !it.isLoopback }
                .map { mapper.fromSystemToModel(it) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}