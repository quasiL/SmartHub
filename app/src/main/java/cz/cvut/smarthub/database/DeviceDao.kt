package cz.cvut.smarthub.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeviceDao {
    @Query("SELECT * FROM devices ORDER BY id ASC")
    fun getAllDevices(): Flow<List<Device>>

    @Query("SELECT * FROM devices WHERE id = :id")
    fun getDeviceById(id: Long): Flow<Device>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevices(vararg devices: Device)

    @Update
    suspend fun updateDevices(vararg devices: Device)

    @Delete
    suspend fun delete(item: Device)

    @Query("DELETE FROM devices")
    suspend fun deleteAll()
}