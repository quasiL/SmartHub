package cz.cvut.smarthub.ui.screens.devices

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.smarthub.database.Device
import cz.cvut.smarthub.database.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeviceDetailViewModel (
    private val deviceRepository: DeviceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val deviceId: Long = checkNotNull(savedStateHandle.get<Long>("ID"))

    val uiState: StateFlow<DeviceUiState> =
        deviceRepository
            .getDeviceForIdStream(deviceId)
            .map {
                if (it != null) {
                    DeviceUiState.Success(it)
                } else if (it == null && deviceId == 0L) {
                    DeviceUiState.Success(Device())
                } else {
                    DeviceUiState.Error(Exception("Device not found"))
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = DeviceUiState.Loading
            )

    fun saveDevice(
        newName: String,
        newType: String,
        newDescription: String,
        newLocation: String,
        newIpAddress: String
    ) {
        viewModelScope.launch {
            deviceRepository.insertDevices(
                Device(
                    id = deviceId,
                    name = newName,
                    type = newType,
                    description = newDescription,
                    location = newLocation,
                    ipAddress = newIpAddress
                )
            )
        }
    }
}

sealed interface DeviceUiState {
    data object Loading : DeviceUiState
    data class Success(val device: Device) : DeviceUiState
    data class Error(val exception: Exception) : DeviceUiState
}