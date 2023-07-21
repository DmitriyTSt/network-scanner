package ru.dmitriyt.networkscanner.presentation.ui.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.dmitriyt.networkscanner.domain.model.NetInterface
import ru.dmitriyt.networkscanner.domain.usecase.GetDevicesUseCase
import ru.dmitriyt.networkscanner.presentation.mapper.NetDeviceMapper
import ru.dmitriyt.networkscanner.presentation.model.LoadableState
import ru.dmitriyt.networkscanner.presentation.model.UiNetDevice
import ru.dmitriyt.networkscanner.presentation.ui.base.BaseViewModel
import ru.dmitriyt.networkscanner.presentation.ui.base.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class NetworkViewModel @Inject constructor(
    private val getDevicesUseCase: GetDevicesUseCase,
    private val netDeviceMapper: NetDeviceMapper,
) : BaseViewModel() {

    /** Доступные устройства */
    private val _devicesDataLiveData = MutableLiveData<List<UiNetDevice>>()
    val devicesDataLiveData: LiveData<List<UiNetDevice>> = _devicesDataLiveData

    /** Процесс сканирования */
    private val _scanStateLiveEvent = SingleLiveEvent<LoadableState<Unit>>()
    val scanStateLiveEvent: LiveData<LoadableState<Unit>> = _scanStateLiveEvent

    /** Текущее сканирование устройств */
    private var collectDevicesJob: Job? = null

    fun loadDevices(netInterface: NetInterface.Connected) {
        collectDevicesJob?.cancel()
        collectDevicesJob = viewModelScope.launch {
            _scanStateLiveEvent.postValue(LoadableState.Loading())
            getDevicesUseCase(GetDevicesUseCase.Params(netInterface)).collectLatest { devices ->
                _devicesDataLiveData.postValue(devices.map { netDeviceMapper.fromDomainToUi(it) })
            }
            _scanStateLiveEvent.postValue(LoadableState.Success(Unit))
        }
    }
}