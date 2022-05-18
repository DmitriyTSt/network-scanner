package ru.dmitriyt.networkscanner.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.data.repository.NetworkRepository
import ru.dmitriyt.networkscanner.domain.usecase.base.UseCaseUnary
import javax.inject.Inject

/**
 * Получение доступных в сети устройств
 */
class GetDevicesUseCase @Inject constructor(
    private val repository: NetworkRepository,
) : UseCaseUnary<GetDevicesUseCase.Params, Flow<List<NetDevice>>>() {

    override suspend fun execute(params: Params): Flow<List<NetDevice>> {
        return repository.getDevices(params.netInterface)
    }

    data class Params(
        val netInterface: NetInterface.Connected,
    )
}