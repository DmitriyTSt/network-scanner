package ru.dmitriyt.networkscanner.presentation.ui.networks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dmitriyt.networkscanner.presentation.model.LoadableState
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.domain.usecase.GetNetInterfacesUseCase
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class NetworkInterfacesViewModel @Inject constructor(
    private val getNetInterfacesUseCase: GetNetInterfacesUseCase,
    private val destinations: NetworkInterfacesDestinations,
) : BaseViewModel() {
    /** Сетевые интерфейсы */
    private val _netInterfacesLiveData = MutableLiveData<LoadableState<List<NetInterface>>>()
    val netInterfacesLiveData: LiveData<LoadableState<List<NetInterface>>> = _netInterfacesLiveData

    fun loadNetInterfaces() {
        _netInterfacesLiveData.launchLoadData { getNetInterfacesUseCase(Unit) }
    }

    fun openNetwork(netInterface: NetInterface.Connected) {
        navigate(destinations.network(netInterface))
    }
}