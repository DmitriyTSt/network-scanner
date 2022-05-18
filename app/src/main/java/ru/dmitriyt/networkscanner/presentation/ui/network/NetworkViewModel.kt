package ru.dmitriyt.networkscanner.presentation.ui.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.dmitriyt.networkscanner.data.model.LoadableState
import ru.dmitriyt.networkscanner.data.model.NetDevice
import ru.dmitriyt.networkscanner.data.model.NetInterface
import ru.dmitriyt.networkscanner.domain.usecase.GetDevicesUseCase
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseViewModel
import ru.dmitriyt.networkscanner.presentation.ui.base.SingleLiveEvent
import javax.inject.Inject

class NetworkViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase,
) : BaseViewModel() {
    /** Состояние получения потока устройств */
    private val _devicesStateLiveData = MutableLiveData<LoadableState<Flow<List<NetDevice>>>>()
    val devicesStateLiveData: LiveData<LoadableState<Flow<List<NetDevice>>>> = _devicesStateLiveData

    /** Доступные устройства */
    private val _devicesDataLiveData = MutableLiveData<List<NetDevice>>()
    val devicesDataLiveData: LiveData<List<NetDevice>> = _devicesDataLiveData

    /** Процесс сканирования */
    private val _scanStateLiveEvent = SingleLiveEvent<LoadableState<Unit>>()
    val scanStateLiveEvent: LiveData<LoadableState<Unit>> = _scanStateLiveEvent

    private var collectDevicesJob: Job? = null

    fun loadDevices(netInterface: NetInterface.Connected) {
        collectDevicesJob?.cancel()
        _devicesStateLiveData.launchLoadData(getDevicesUseCase.executeFlow(GetDevicesUseCase.Params(netInterface)).map { state ->
            state.doOnSuccess { devicesFlow ->
                collectDevicesJob = viewModelScope.launch {
                    _scanStateLiveEvent.postValue(LoadableState.Loading())
                    devicesFlow.collect { devices ->
                        _devicesDataLiveData.postValue(devices)
                    }
                    _scanStateLiveEvent.postValue(LoadableState.Success(Unit))
                }
            }
            state
        })
    }
}