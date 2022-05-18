package ru.dmitriyt.networkscanner.presentation.ui.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.dmitriyt.networkscanner.data.model.LoadableState
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.domain.usecase.GetDevicesUseCase
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseViewModel
import javax.inject.Inject

class NetworkViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase,
) : BaseViewModel() {
    /** Доступные устройства */
    private val _devicesLiveData = MutableLiveData<LoadableState<List<NetDevice>>>()
    val devicesLiveData: LiveData<LoadableState<List<NetDevice>>> = _devicesLiveData

    fun loadDevices(netInterface: NetInterface.Connected) {
        _devicesLiveData.launchLoadData(getDevicesUseCase.executeFlow(GetDevicesUseCase.Params(netInterface)))
    }
}