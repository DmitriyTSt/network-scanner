package ru.dmitriyt.networkscanner.presentation.mapper

import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.domain.repository.ResourceRepository
import ru.dmitriyt.networkscanner.presentation.model.UiNetInterface
import javax.inject.Inject

class NetInterfaceMapper @Inject constructor(
    private val resourceRepository: ResourceRepository,
) : DomainToUiMapper<NetInterface, UiNetInterface> {

    override fun fromDomainToUi(domain: NetInterface): UiNetInterface {
        return UiNetInterface(
            name = domain.name,
            address = when (domain) {
                is NetInterface.Connected -> "${domain.currentDevice.host}/${domain.prefixLength}"
                is NetInterface.Disconnected -> resourceRepository.getString(R.string.net_interface_not_connected_status)
            },
            statusIconRes = if (domain.isUp) {
                R.drawable.ic_network_interface_up
            } else {
                R.drawable.ic_network_interface_down
            },
            isLoopback = domain.isLoopback,
            isConnected = domain is NetInterface.Connected,
            domainModel = domain,
        )
    }
}