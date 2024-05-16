package cz.cvut.smarthub.ui.screens.devices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.smarthub.database.Device
import cz.cvut.smarthub.database.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import cz.cvut.smarthub.model.common.Result
import cz.cvut.smarthub.model.common.asResult

class DevicesViewModel (
    private val deviceRepository: DeviceRepository,
) : ViewModel() {

    val devices : StateFlow<DevicesUiState> =
        deviceRepository
            .getAllDevicesStream()
            .asResult()
            .map { allDevicesToResult ->
                when (allDevicesToResult) {
                    is Result.Error -> DevicesUiState.Error(Exception(allDevicesToResult.exception))
                    is Result.Loading -> DevicesUiState.Loading
                    is Result.Success -> DevicesUiState.Success(allDevicesToResult.data)
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = DevicesUiState.Loading
            )

    fun addDevice(device: Device) {
        viewModelScope.launch {
            deviceRepository.insertDevices(
                device
            )
        }
    }

    fun deleteAllDevices() {
        viewModelScope.launch {
            deviceRepository.deleteAllDevices()
        }
    }

}

sealed interface DevicesUiState {
    data object Loading : DevicesUiState
    data class Success(val itemList: List<Device> = listOf()) : DevicesUiState
    data class Error(val exception: Exception) : DevicesUiState
}