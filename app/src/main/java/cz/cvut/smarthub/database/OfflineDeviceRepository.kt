package cz.cvut.smarthub.database

import kotlinx.coroutines.flow.Flow

class OfflineDeviceRepository(private val deviceDao: DeviceDao) : DeviceRepository {
    override fun getAllDevicesStream(): Flow<List<Device>> = deviceDao.getAllDevices()
    override fun getDeviceForIdStream(id: Long): Flow<Device?> = deviceDao.getDeviceById(id)
    override suspend fun insertDevices(vararg devices: Device) = deviceDao.insertDevices(*devices)
    override suspend fun deleteDevice(device: Device) = deviceDao.delete(device)
    override suspend fun deleteAllDevices() = deviceDao.deleteAll()
    override suspend fun updateDevices(vararg devices: Device) = deviceDao.updateDevices(*devices)
}