package ru.dmitriyt.networkscanner.presentation.ui.networks

import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.presentation.navigation.Destination
import ru.dmitriyt.networkscanner.presentation.ui.network.NetworkFragment
import javax.inject.Inject

class NetworkInterfacesDestinations @Inject constructor() {
    /** Сеть */
    fun network(netInterface: NetInterface.Connected) = Destination.Fragment(
        NetworkFragment.newInstance(netInterface)
    )
}