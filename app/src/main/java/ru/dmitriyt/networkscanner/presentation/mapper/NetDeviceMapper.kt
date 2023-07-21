package ru.dmitriyt.networkscanner.presentation.mapper

import ru.dmitriyt.networkscanner.R
import ru.dmitriyt.networkscanner.domain.model.NetDevice
import ru.dmitriyt.networkscanner.domain.repository.ResourceRepository
import ru.dmitriyt.networkscanner.presentation.model.UiNetDevice
import javax.inject.Inject

class NetDeviceMapper @Inject constructor(
    private val resourceRepository: ResourceRepository,
) : DomainToUiMapper<NetDevice, UiNetDevice> {

    override fun fromDomainToUi(domain: NetDevice): UiNetDevice {
        return UiNetDevice(
            host = domain.host,
            addressUInt = domain.addressUInt,
            hostName = domain.hostName ?: resourceRepository.getString(R.string.unknown_device),
            iconRes = if (domain.isCurrentDevice) {
                R.drawable.ic_device_current
            } else {
                R.drawable.ic_device_hub
            },
            mac = domain.mac,
        )
    }
}