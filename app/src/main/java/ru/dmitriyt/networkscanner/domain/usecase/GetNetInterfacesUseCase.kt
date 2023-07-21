package ru.dmitriyt.networkscanner.domain.usecase

import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.domain.repository.NetworkRepository
import ru.dmitriyt.networkscanner.domain.usecase.base.UseCaseUnary
import javax.inject.Inject

/**
 * Получение сетевых интерфейсов
 */
class GetNetInterfacesUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
) : UseCaseUnary<Unit, List<NetInterface>>() {

    override suspend operator fun invoke(params: Unit): List<NetInterface> {
        return networkRepository.getNetInterfaces()
    }
}