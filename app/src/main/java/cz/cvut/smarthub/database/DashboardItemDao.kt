package cz.cvut.smarthub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DashboardItemDao {
    @Query("SELECT * FROM dashboard_items")
    fun getAllDashboardItems(): Flow<List<DashboardItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDashboardItems(vararg items: DashboardItem)

    @Query("DELETE FROM dashboard_items")
    suspend fun deleteAllDashboardItems()
}