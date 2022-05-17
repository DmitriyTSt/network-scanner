package ru.dmitriyt.networkscanner.domain.usecase

import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.data.repository.NetworkRepository
import ru.dmitriyt.networkscanner.domain.usecase.base.UseCaseUnary
import javax.inject.Inject

/**
 * Получение сетевых интерфейсов
 */
class GetNetInterfacesUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
) : UseCaseUnary<Unit, List<NetInterface>>() {
    override suspend fun execute(params: Unit): List<NetInterface> {
        return networkRepository.getNetInterfaces()
    }
}