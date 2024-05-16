package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getAllDevicesStream(): Flow<List<Device>>
    fun getDeviceForIdStream(id: Long): Flow<Device?>
    suspend fun insertDevices(vararg devices: Device)
    suspend fun deleteDevice(device: Device)
    suspend fun deleteAllDevices()
    suspend fun updateDevices(vararg devices: Device)
}